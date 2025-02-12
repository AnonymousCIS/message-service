package org.anonymous.message.controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.anonymous.member.Member;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestMessage {
    /**
     * 메일을 받는 쪽 이메일
     *      필수가 되는 조건 : 회원이 다른 회원에게 쪽지를 보내는 경우
     *      필수가 아닌 조건 : 관리자가 공지사항(notice)으로 쪽지를 보내는 경우
     * */
    private String receiverEmail;

    private boolean notice; // 쪽지 공지

//    private String gid; // 파일 아이디

    @NotBlank
    private String subject; // 쪽지 제목

    @NotBlank
    private String content; // 쪽지 내용

}
