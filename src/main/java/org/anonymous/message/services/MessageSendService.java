package org.anonymous.message.services;

import lombok.RequiredArgsConstructor;
import org.anonymous.global.libs.Utils;
import org.anonymous.member.Member;
import org.anonymous.member.MemberUtil;
import org.anonymous.message.constants.MessageStatus;
import org.anonymous.message.controllers.RequestMessage;
import org.anonymous.message.entities.Message;
import org.anonymous.message.repositories.MessageRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;


@Lazy
@Service
@RequiredArgsConstructor
public class MessageSendService {

    private final Utils utils;
    private final MemberUtil  memberUtil;
    private final RestTemplate restTemplate;
    private final MessageRepository repository;

    public Message process(RequestMessage form) {

        String token = utils.getAuthToken();
        HttpHeaders headers = new HttpHeaders();
        if (StringUtils.hasText(token)) {
            headers.setBearerAuth(token);
        }

        HttpEntity<Member> request = new HttpEntity<>(headers);

        String apiUrl = utils.serviceUrl("member-service", "/info/" + form.getEmail());
        ResponseEntity<Member> item = restTemplate.exchange(URI.create(apiUrl), HttpMethod.GET, request, Member.class);
        item = !form.isNotice() ? item : null;
        System.out.println("item" + item);


        Message message = Message.builder()
                .notice(form.isNotice()) // 공지
                .subject(form.getSubject()) // 제목
                .content(form.getContent()) // 내용
                .senderEmail(memberUtil.getMember().getEmail())
                .senderName(memberUtil.getMember().getName())
                .receiverEmail(item.getBody().getEmail())
                .receiverName(item.getBody().getName())
                .status(MessageStatus.UNREAD)
                .build();

        repository.saveAndFlush(message);

        return message;
    }
}
