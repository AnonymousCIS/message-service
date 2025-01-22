package org.anonymous.message.validators;

import lombok.RequiredArgsConstructor;
import org.anonymous.member.Member;
import org.anonymous.member.MemberUtil;
import org.anonymous.message.controllers.RequestMessage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Lazy
@Component
@RequiredArgsConstructor
public class MessageValidator implements Validator {

    private final MemberUtil memberUtil;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestMessage.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

        RequestMessage form = (RequestMessage) target;
        String email = String.valueOf(form.getEmail());
        boolean notice = form.isNotice();

        if (!memberUtil.isAdmin() && notice) { // 어드민이 아니고 공지로 쓴다면
            notice = false;
            form.setNotice(notice);
        }

        if (!memberUtil.isAdmin() && !notice && !StringUtils.hasText(email)) {
            errors.rejectValue("email", "NotBlank");
        }

        /*if (!notice && !memberRepository.exists(email)) {
            errors.reject("NotFound.member");
        }*/

    }
}
