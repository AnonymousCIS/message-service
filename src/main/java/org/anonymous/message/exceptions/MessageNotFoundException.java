package org.anonymous.message.exceptions;

import org.anonymous.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class MessageNotFoundException extends CommonException {
    public MessageNotFoundException() {
        super("NotFound.message", HttpStatus.NOT_FOUND);
        setErrorCode(true);
    }
}
