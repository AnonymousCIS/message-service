package org.anonymous.message.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anonymous.global.exceptions.BadRequestException;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.rests.JSONData;
import org.anonymous.member.MemberUtil;
import org.anonymous.message.entities.Message;
import org.anonymous.message.services.*;
import org.anonymous.message.validators.MessageValidator;
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
    @PostMapping("/write")
    public JSONData write(@Valid @RequestBody RequestMessage form, Errors errors, HttpServletRequest request) {
        commonProcess("write");

        messageValidator.validate(form, errors);

        if (errors.hasErrors()) {
            throw new BadRequestException();
        }

        Message message = sendService.process(form);
        long totalUnRead = messageCountService.totalUnRead(form.getEmail());
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
    @GetMapping("/view/{seq}")
    public JSONData view(@PathVariable("seq") Long seq) {
        commonProcess("view");


        infoService.get(seq);

        statusService.change(seq);

        return null;
    }

    /**
     * 쪽지 목록 조회
     *
     * @return
     */
    @GetMapping("/list")
    public JSONData list(@ModelAttribute MessageSearch search) {
        commonProcess("list");


        infoService.getList(search);

        return null;
    }

    /**
     * 쪽지 미열람 개수 확인
     *
     * @return
     */
    @GetMapping("/count")
    public JSONData count() {

        messageCountService.totalUnRead();

        return null;
    }

    /**
     * 쪽지 단일 | 목록 삭제
     *
     * @return
     */
    @PatchMapping("/deletes")
    public JSONData deletes() {

        deleteService.process();

        return null;
    }

    private void commonProcess(String mode) {

    }

}
