package org.anonymous.message.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.anonymous.global.rests.JSONData;
import org.anonymous.message.entities.Message;
import org.anonymous.message.services.admins.MessageConfigDeleteService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class MessageAdminController {

    private final MessageConfigDeleteService deleteService;

    @Operation(summary = "쪽지 단일 & 목록 일괄 삭제 처리", description = "쪽지를 단일 & 목록으로 삭제합니다. (관리자용 삭제는 DB에서 삭제O)")
    @Parameter(name = "seq", description = "쪽지 ID", required = true, example = "1")
    @DeleteMapping("/deletes")
    public JSONData deletes(@RequestParam("seq") List<Long> seqs) {

        List<Message> item = deleteService.deletes(seqs);

        return new JSONData(item);
    }
}
