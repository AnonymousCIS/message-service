package org.anonymous.message.controllers;

import lombok.RequiredArgsConstructor;
import org.anonymous.global.rests.JSONData;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class MessageAdminController {

    @DeleteMapping("/deletes")
    public JSONData deletes() {

        return null;
    }
}
