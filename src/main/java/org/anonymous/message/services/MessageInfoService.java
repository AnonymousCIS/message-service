package org.anonymous.message.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.paging.ListData;
import org.anonymous.global.paging.Pagination;
import org.anonymous.member.Member;
import org.anonymous.member.MemberUtil;
import org.anonymous.message.controllers.MessageSearch;
import org.anonymous.message.entities.Message;
import org.anonymous.message.entities.QMessage;
import org.anonymous.message.exceptions.MessageNotFoundException;
import org.anonymous.message.repositories.MessageRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class MessageInfoService {

    private final MemberUtil memberUtil;
    private final MessageRepository messageRepository;
    private final HttpServletRequest request;
    private final JPAQueryFactory queryFactory;
    private final Utils utils;

    /**
     * 쪽지 하나 조회
     * @param seq
     * @return
     */
    public Message get(Long seq) {
        BooleanBuilder builder = new BooleanBuilder();
        BooleanBuilder orBuilder = new BooleanBuilder();
        QMessage message = QMessage.message;
        builder.and(message.seq.eq(seq));

        if (!memberUtil.isAdmin()) {
            Member member = memberUtil.getMember();
            BooleanBuilder orBuilder2 = new BooleanBuilder();
            BooleanBuilder andBuilder = new BooleanBuilder();
            orBuilder2.or(andBuilder.and(message.notice.eq(true)).and(message.receiverEmail.isNull()))
                    .or(message.receiverEmail.eq(member.getEmail()));

            orBuilder.or(message.senderEmail.eq(member.getEmail())
                    .or(orBuilder2));

            builder.and(orBuilder);
        }

        Message item = messageRepository.findOne(builder).orElseThrow(MessageNotFoundException::new);

        return item;
    }

    public ListData<Message> getList(MessageSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;
        int offset =(page - 1) * limit;

//        검색 조건 처리 S
        BooleanBuilder andBuilder = new BooleanBuilder();
        QMessage message = QMessage.message;
        String mode = search.getMode();
        Member member = memberUtil.getMember();

        mode = StringUtils.hasText(mode) ? mode : "receive";
//        send - 보낸 쪽지 목록, receive - 받은 쪽지 목록
        if (mode.equals("send")) {
            andBuilder.and(message.senderEmail.eq(member.getEmail()));
        } else {
            BooleanBuilder orBuilder = new BooleanBuilder();
            BooleanBuilder andBuilder2 = new BooleanBuilder();

            orBuilder.or(andBuilder2.and(message.notice.eq(true)).and(message.receiverEmail.isNull())) //공지 쪽지
                    .or(message.receiverEmail.eq(member.getEmail()));

            andBuilder.and(orBuilder);
        }

        andBuilder.and(mode.equals("send") ? message.deletedBySender.eq(false) : message.deletedByReceiver.eq(false));

//        보낸 사람 조건 검색
        List<String> sender =search.getSender();
        if (mode.equals("receive") && sender != null && !sender.isEmpty()) {
            andBuilder.and(message.senderEmail.in(sender));
        }

//        키워드 검색 처리
        String sopt = search.getSopt();
        String skey = search.getSkey();
        sopt = StringUtils.hasText(sopt) ? sopt : "ALL";
        if (StringUtils.hasText(skey)) {
            StringExpression condition = sopt.equals("SUBJECT") ? message.subject : message.subject.concat(message.content);
            andBuilder.and(condition.contains(skey.trim()));
        }

//        검색 조건 처리 E

        List<Message> items = queryFactory.selectFrom(message)
                .where(andBuilder)
                .limit(limit)
                .offset(offset)
                .orderBy(message.notice.desc(), message.createdAt.desc())
                .fetch();

        long total = messageRepository.count(andBuilder);
        Pagination pagination = new Pagination(page, (int) total, utils.isMobile() ? 5 : 10, limit, request);

        return new ListData<>(items, pagination);
    }
}
