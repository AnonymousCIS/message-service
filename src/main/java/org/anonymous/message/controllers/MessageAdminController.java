package org.anonymous.message.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.anonymous.global.rests.JSONData;
import org.anonymous.message.entities.Message;
import org.anonymous.message.services.MessageStatusService;
import org.anonymous.message.services.admins.MessageConfigDeleteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Message Admin API", description = "Message Admin 기능")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class MessageAdminController {

    private final MessageConfigDeleteService deleteService;
    private final MessageStatusService statusService;

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
     * 보낸 사람 이메일 블락 처리
     * @param email
     */
    @PatchMapping("/block")
    public void block(@RequestParam("email") String email) {

        statusService.block(email);
    }

    /**
     * 보낸 사람 이메일 언블락 처리
     * @param email
     */
    @PatchMapping("/unBlock")
    public void unBlock(@RequestParam("email") String email) {

         statusService.unBlock(email);
    }
}
