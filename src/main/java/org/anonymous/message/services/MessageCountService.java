package org.anonymous.message.services;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.anonymous.member.Member;
import org.anonymous.member.MemberUtil;
import org.anonymous.message.constants.MessageStatus;
import org.anonymous.message.entities.QMessage;
import org.anonymous.message.repositories.MessageRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
@RequiredArgsConstructor
public class MessageCountService {

    private final MessageRepository messageRepository;
    private final MemberUtil memberUtil;

    public long totalUnRead(String email) {
        BooleanBuilder andBuilder = new BooleanBuilder();
        QMessage message = QMessage.message;
        andBuilder.and(message.receiverEmail.eq(email))
                .and(message.status.eq(MessageStatus.UNREAD));

        return messageRepository.count(andBuilder);
    }

    public long totalUnRead() {
        Member member = memberUtil.getMember();

        return totalUnRead(member.getEmail());
    }

}
