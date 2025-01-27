package org.anonymous.message.validators;

import lombok.RequiredArgsConstructor;
import org.anonymous.global.libs.Utils;
import org.anonymous.member.MemberUtil;
import org.anonymous.message.controllers.RequestMessage;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.client.RestTemplate;

import java.net.URI;


@Lazy
@Component
@RequiredArgsConstructor
public class MessageValidator implements Validator {

    private final MemberUtil memberUtil;
    private final Utils utils;
    private final RestTemplate restTemplate;

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
        String email = form.getEmail();
        boolean notice = form.isNotice();

        if (!memberUtil.isAdmin() && notice) {
            notice = false;
            form.setNotice(notice);
        }

        if (!memberUtil.isAdmin() && !notice && !StringUtils.hasText(email)) {
            errors.rejectValue("email", "NotBlank");
        }

        String token = utils.getAuthToken();
        HttpHeaders headers = new HttpHeaders();
        if (StringUtils.hasText(token)) {
            headers.setBearerAuth(token);
        }

        HttpEntity<Void> request = new HttpEntity<>(headers);

        String apiUrl = utils.serviceUrl("member-service", "/exists/" + form.getEmail());
        ResponseEntity<Void> item = restTemplate.exchange(URI.create(apiUrl), HttpMethod.GET, request, Void.class);
        if (!notice && item.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            errors.reject("NotFound.member");
        }

        /*if (!notice && !memberRepository.exists(email)) {
            errors.reject("NotFound.member");
        }*/

    }
}
