package org.anonymous.message.controllers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.anonymous.member.Member;

@Data
public class RequestMessage {

    private Member email;

    private Member name;

    private boolean notice; // 공지

    private String gid; // 파일 아이디

    @NotBlank
    private String subject; // 제목

    @NotBlank
    private String content; // 내용

}
