package org.anonymous.message.services;

import lombok.RequiredArgsConstructor;
import org.anonymous.global.exceptions.BadRequestException;
import org.anonymous.global.exceptions.UnAuthorizedException;
import org.anonymous.member.MemberUtil;
import org.anonymous.message.constants.MessageStatus;
import org.anonymous.message.entities.Message;
import org.anonymous.message.repositories.MessageRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class MessageStatusService {

    private final MessageInfoService infoService;
    private final MessageRepository repository;
    private final MemberUtil memberUtil;

    public void change(Long seq) {
        Message item = infoService.get(seq);
        if (item.isReceived()) { // 수신한 메세지만 열람 상태로 변경
            item.setStatus(MessageStatus.READ);
        }

        repository.saveAndFlush(item);
    }

    public void status(List<String> emails, boolean status) {

        if (emails.isEmpty() || emails == null) {
            // 예외처리
            throw new BadRequestException();
        }

        for (String email : emails) {
            List<Message> items = repository.findAllByCreatedBy(email);

//        어드민이 아닐경우 권한없음
            if (!memberUtil.isAdmin()) {
                throw new UnAuthorizedException();
            }

            for (Message item : items) {

                item.setBlock(status);
            }
        }

    }
}
