package org.anonymous.message.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.anonymous.global.paging.ListData;
import org.anonymous.global.rests.JSONData;
import org.anonymous.message.entities.Message;
import org.anonymous.message.services.MessageInfoService;
import org.anonymous.message.services.MessageStatusService;
import org.anonymous.message.services.admins.MessageConfigDeleteService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Message Admin API", description = "Message Admin 기능")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class MessageAdminController {

    private final MessageConfigDeleteService deleteService;
    private final MessageStatusService statusService;
    private final MessageInfoService infoService;

    /**
     * 쪽지 목록 조회
     *
     * @return
     */
    @Operation(summary = "쪽지 목록 조회", description = "쪽지 목록으로 조회합니다.")
    @Parameter(name = "status", description = "상태(열람, 미열람)별 조회", examples = {
            @ExampleObject(name = "READ", value = "READ", description = "열람"),
            @ExampleObject(name = "UNREAD", value = "UNREAD", description = "미열람")
    })
    @GetMapping("/list")
    public JSONData list(@ModelAttribute MessageSearch search) {


        ListData<Message> data = infoService.getAdminList(search);

        return new JSONData(data);
    }

    /**
     * 어드민 쪽지 단일 & 목록 일괄 삭제
     *
     * @param seqs
     * @return
     */
    @Operation(summary = "쪽지 단일 & 목록 일괄 삭제 처리", description = "쪽지를 단일 & 목록으로 삭제합니다. (관리자용 삭제는 DB에서 삭제O)")
    @Parameter(name = "seq", description = "쪽지 ID", required = true, example = "1")
    @DeleteMapping("/deletes")
    public JSONData deletes(@RequestParam("seq") List<Long> seqs) {

        List<Message> item = deleteService.deletes(seqs);

        return new JSONData(item);
    }

    /**
     * 보낸 사람 이메일 블락, 언블락 처리
     * @param emails
     * @param status
     */
    @Operation(summary = "보낸 사람 이메일 블락 처리", description = "보낸 사람의 이메일을 블락, 언블락 처리합니다.")
    @Parameters({
            @Parameter(name = "emails", description = "보낸 사람 이메일", required = true, example = "user01@test.org"),
            @Parameter(name = "status", description = "블락, 언블락", examples = {
                    @ExampleObject(name = "true", value = "true", description = "블락"),
                    @ExampleObject(name = "false", value = "false", description = "언블락")
            })
    })
    @PatchMapping("/status")
    public void status(@RequestBody List<String> emails, @RequestParam("status") boolean status) {
        if (emails == null) {
            emails = new ArrayList<>(); // 기본값 설정
        }
        System.out.println(status);
        System.out.println("Emails"+ emails);
        statusService.status(emails, status);
    }
}
