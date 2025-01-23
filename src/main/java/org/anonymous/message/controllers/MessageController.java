package org.anonymous.message.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anonymous.global.exceptions.BadRequestException;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.rests.JSONData;
import org.anonymous.member.MemberUtil;
import org.anonymous.message.services.*;
import org.anonymous.message.validators.MessageValidator;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final Utils utils;
    private final MessageValidator messageValidator;
    private final MessageInfoService messageInfoService;
    private final MessageSendService messageSendService;
    private final MessageCountService messageCountService;
    private final MessageStatusService messageStatusService;
    private final MessageDeleteService messageDeleteService;

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
    public JSONData write(@Valid @RequestBody RequestMessage form, Errors errors) {
        commonProcess("write");

        messageValidator.validate(form, errors);

        if (errors.hasErrors()) {
            throw new BadRequestException();
        }

        messageSendService.process();

        return null;
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


        messageInfoService.get();

        messageStatusService.change();

        return null;
    }

    /**
     * 쪽지 목록 조회
     *
     * @return
     */
    @GetMapping("/list")
    public JSONData list() {
        commonProcess("list");


        messageInfoService.getList();

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

        messageDeleteService.process();

        return null;
    }

    private void commonProcess(String mode) {

    }

}
