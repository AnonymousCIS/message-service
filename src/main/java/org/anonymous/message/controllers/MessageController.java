package org.anonymous.message.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anonymous.global.exceptions.BadRequestException;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.paging.ListData;
import org.anonymous.global.rests.JSONData;
import org.anonymous.member.MemberUtil;
import org.anonymous.message.entities.Message;
import org.anonymous.message.services.*;
import org.anonymous.message.validators.MessageValidator;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final Utils utils;
    private final MessageValidator messageValidator;
    private final MessageInfoService infoService;
    private final MessageSendService sendService;
    private final MessageCountService messageCountService;
    private final MessageStatusService statusService;
    private final MessageDeleteService deleteService;
    private final ObjectMapper om;

        /*
    * - POST /write : 쪽지 작성
- GET /view/{seq} : 쪽지 단일 조회
- GET /list : 쪽지 목록
- GET /count : 쪽지 미열람 개수 확인
- PATCH /deletes : 쪽지 단일 | 목록 삭제
    * */


    /**
     * 검증 실패시 JSON으로 응답
     * @param form
     * @param errors
     * @return
     */
    /*@ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/upload")
    public JSONData upload(@RequestBody @Valid RequestMessage form, Errors errors) {


        return null;
    }*/

    /**
     * 쪽지 작성
     *
     * @return
     */
    @Operation(summary = "쪽지 작성 처리", description = "쪽지를 작성합니다.")
    @Parameters({
            @Parameter(name = "form", description = "쪽지 작성 양식"),
            @Parameter(name = "seq", description = "쪽지 ID", example = "1"),
            @Parameter(name = "receiverEmail", description = "쪽지 받는 사람 이메일", example = "user01@test.org"),
            @Parameter(name = "notice", description = "쪽지 공지", examples = {
                    @ExampleObject(name = "true", value = "true", description = "공지 쪽지 O"),
                    @ExampleObject(name = "false", value = "false", description = "공지 쪽지 X")
            }),
            @Parameter(name = "subject", description = "쪽지 제목", example = "제목"),
            @Parameter(name = "content", description = "쪽지 내용", example = "내용")
    })
    @PostMapping("/write")
    public JSONData write(@Valid @RequestBody RequestMessage form, Errors errors, HttpServletRequest request) {

        messageValidator.validate(form, errors);

        if (errors.hasErrors()) {
            throw new BadRequestException();
        }

        Message message = sendService.process(form);
        long totalUnRead = messageCountService.totalUnRead(form.getReceiverEmail());
        Map<String, Object> data = new HashMap<>();
        data.put("item", message);
        data.put("totalUnRead", totalUnRead);

        StringBuffer sb = new StringBuffer();

        try{
            String json = om.writeValueAsString(data);
            sb.append(String.format("if (typeof webSocket != undefined) { webSocket.onopen = () => webSocket.send('%s'); }", json));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        sb.append(String.format("location.replace('%s')", request.getContextPath() + "/list"));

        return new JSONData(data);
    }

    /**
     * 쪽지 단일 조회
     *
     * @param seq
     * @return
     */
    @Operation(summary = "쪽지 단일 조회", description = "쪽지 ID로 단일 조회합니다.")
    @Parameter(name = "seq", description = "쪽지 ID", example = "1")
    @GetMapping("/view/{seq}")
    public JSONData view(@PathVariable("seq") Long seq) {

//        조회
        Message item = infoService.get(seq);

//        미열람 -> 열람 변환
        statusService.change(seq);

        return new JSONData(item);
    }

    /**
     * 쪽지 목록 조회
     *
     * @return
     */
    @Operation(summary = "쪽지 목록 조회", description = "쪽지 목록으로 조회합니다.")
    @Parameters({
            @Parameter(name = "mode", description = "쪽지 보낸 사람, 받는 사람별 쪽지 목록", examples = {
                    @ExampleObject(name = "send", value = "send", description = "보낸 사람"),
                    @ExampleObject(name = "receive", value = "receive", description = "받는 사람")
            }),
            @Parameter(name = "sender", description = "보낸 사람 조건"),
            @Parameter(name = "status", description = "상태(열람, 미열람)별 조회", examples = {
                    @ExampleObject(name = "READ", value = "READ", description = "열람"),
                    @ExampleObject(name = "UNREAD", value = "UNREAD", description = "미열람")
            })
    })
    @GetMapping("/list")
    public JSONData list(@ModelAttribute MessageSearch search) {


        ListData<Message> item = infoService.getList(search);

        return new JSONData(item);
    }

    /**
     * 쪽지 미열람 개수 확인
     *
     * @return
     */
    @Operation(summary = "미열람 개수", description = "미열람 쪽지 개수를 확인합니다.")
    @Parameter(name = "receiverEmail", description = "쪽지 받는 사람 이메일")
    @GetMapping("/count")
    public JSONData count(RequestMessage form) {

        Long count = messageCountService.totalUnRead(form.getReceiverEmail());

        return new JSONData(count);
    }

    /**
     * 쪽지 단일 | 목록 삭제
     *
     * @return
     */
    @Operation(summary = "쪽지 단일 & 목록 삭제", description = "쪽지를 단일 & 목록으로 삭제합니다. (일반 사용자용 삭제는 DB에서 삭제X, DeletedAt으로 현재 시간 부여)")
    @Parameter(name = "seq", description = "쪽지 ID")
    @PatchMapping("/deletes")
    public JSONData deletes(@RequestParam("seq") List<Long> seqs, @RequestParam(name = "mode", defaultValue = "receive") String mode) {

        List<Message> items = deleteService.deletes(seqs, mode);

        return new JSONData(items);
    }

}
