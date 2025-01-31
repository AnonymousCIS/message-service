package org.anonymous.message.services;

import lombok.RequiredArgsConstructor;
import org.anonymous.global.exceptions.UnAuthorizedException;
import org.anonymous.member.MemberUtil;
import org.anonymous.message.constants.MessageStatus;
import org.anonymous.message.entities.Message;
import org.anonymous.message.entities.MessageBlock;
import org.anonymous.message.repositories.MessageBlockRepository;
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
    private final MessageBlockRepository blockRepository;
    private final MemberUtil memberUtil;

    public void change(Long seq) {
        Message item = infoService.get(seq);
        if (item.isReceived()) { // 수신한 메세지만 열람 상태로 변경
            item.setStatus(MessageStatus.READ);
        }

        repository.saveAndFlush(item);
    }

    public void block(String email) {
        List<Message> items = repository.findAllByCreatedBy(email);

//        어드민이 아닐경우 권한없음
        if (!memberUtil.isAdmin()) {
            throw new UnAuthorizedException();
        }

        for (Message item : items) {
            item.setStatus(MessageStatus.BLOCK);
            MessageBlock messageBlock = new MessageBlock();
            messageBlock.setSeq(item.getSeq());
            messageBlock.setStatus(item.getStatus());
            messageBlock.setType("email");
            messageBlock.setEmail(item.getSenderEmail());
            blockRepository.save(messageBlock);
        }
        blockRepository.flush();

    }

    public void unBlock(String email) {
        List<Message> items = repository.findAllByCreatedBy(email);

//        어드민이 아닐경우 권한없음
        if (!memberUtil.isAdmin()) {
            throw new UnAuthorizedException();
        }

        for (Message item : items) {
            item.setStatus(MessageStatus.ALL);
            MessageBlock messageBlock = new MessageBlock();
            messageBlock.setSeq(item.getSeq());
            messageBlock.setStatus(item.getStatus());
            messageBlock.setType("email");
            messageBlock.setEmail(item.getReceiverEmail());
            blockRepository.save(messageBlock);
        }
        blockRepository.flush();
    }
}
