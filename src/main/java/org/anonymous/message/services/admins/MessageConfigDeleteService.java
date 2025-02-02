package org.anonymous.message.services.admins;

import lombok.RequiredArgsConstructor;
import org.anonymous.message.entities.Message;
import org.anonymous.message.repositories.MessageRepository;
import org.anonymous.message.services.MessageInfoService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class MessageConfigDeleteService {

    private final MessageRepository repository;
    private final MessageInfoService infoService;

//    메세지 단일 삭제
    public Message delete(Long seq) {
        Message message = infoService.get(seq);
        if (message != null) {
            repository.delete(message);
            repository.flush();
        }
        return message;
    }

    public List<Message> deletes(List<Long> seqs) {
        List<Message> messages = new ArrayList<>();
        for (Long seq : seqs) {
            Message item = delete(seq);
            if (item != null) {
                messages.add(item);
            }
        }
        return messages;
    }
}
