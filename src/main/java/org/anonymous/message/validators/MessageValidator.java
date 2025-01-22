package org.anonymous.message.validators;

import org.anonymous.message.controllers.RequestMessage;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class MessageValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestMessage.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RequestMessage form = (RequestMessage) target;
        boolean notice = form.isNotice();

    }
}
