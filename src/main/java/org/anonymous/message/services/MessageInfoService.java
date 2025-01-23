package org.anonymous.message.services;

import lombok.RequiredArgsConstructor;
import org.anonymous.member.Member;
import org.anonymous.message.entities.Message;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class MessageInfoService {

    public Message get() {

        return null;
    }

    public List<Member> getList() {

        return null;
    }
}
