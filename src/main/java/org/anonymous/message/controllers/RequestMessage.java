package org.anonymous.message.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestMessage {

    private boolean notice;

    @NotBlank
    private String gid;

    @NotBlank
    private String subject;

    @NotBlank
    private String content;

}
