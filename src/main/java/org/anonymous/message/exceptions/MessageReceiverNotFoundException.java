package org.anonymous.message.exceptions;

import org.anonymous.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class MessageReceiverNotFoundException extends CommonException {
    public MessageReceiverNotFoundException() {
        super("NotFound.receiver", HttpStatus.NOT_FOUND);
        setErrorCode(true);
    }
}
