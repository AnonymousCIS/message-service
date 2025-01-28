package org.anonymous.message.controllers;

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

    @DeleteMapping("/deletes")
    public JSONData deletes(@RequestParam("seq") List<Long> seqs) {

        List<Message> item = deleteService.deletes(seqs);

        return new JSONData(item);
    }
}
