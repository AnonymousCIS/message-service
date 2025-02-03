package org.anonymous.message.services;

import lombok.RequiredArgsConstructor;
import org.anonymous.global.exceptions.UnAuthorizedException;
import org.anonymous.member.MemberUtil;
import org.anonymous.message.entities.Message;
import org.anonymous.message.repositories.MessageRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class MessageDeleteService {

    private final MemberUtil memberUtil;
    private final MessageInfoService infoService;
    private final MessageRepository repository;
    private final MessageStatusService statusService;

    /**
     * 삭제 처리
     * 0. 공지인 경우는 관리자인 경우만 삭제
     * 1. sender 쪽에서 삭제하는 경우 / mode - send
     *      deletedBySender 값을 true
     * 2. receiver 쪽에서 삭제하는 경우 / mode - receive
     *      deletedByReceiver 값을 true
     * 3. deletedBySender와 deletedByReceiver가 모두 true인 경우 실제 DB에서도 삭제(Message 쪽 삭제, 파일 데이터 함께 삭제)
     * @param seq
     */
    public Message delete(Long seq, String mode) {
        mode = StringUtils.hasText(mode) ? mode : "receive";

        boolean isProceedDelete = false;
        Message message = infoService.get(seq);
        if (message.isNotice()) { // 공지
            if (memberUtil.isAdmin()) { // 공지이고 관리자인 경우 // 삭제 처리
                isProceedDelete = true;
            } else { // 공지이지만 관리자가 아닌 경우 - 권한 없음
                throw new UnAuthorizedException();
            }
        } // end if

        if (mode.equals("send")) { //보낸쪽
            message.setDeletedBySender(true);
            message.setDeletedAt(LocalDateTime.now());
        }
        if (mode.equals("receive")) { // 받는쪽
            message.setDeletedByReceiver(true);
            message.setDeletedAt(LocalDateTime.now());
        }
        if (message.isDeletedBySender() && message.isDeletedByReceiver()) { // 보낸쪽, 받는쪽 모두 삭제 한 경우 안보이게 처리
            repository.saveAndFlush(message);
        }

//        삭제 진행이 필요한 경우
        if (isProceedDelete) {

            //DB에서 삭제
            repository.delete(message);
            repository.flush();
        } else { // 보내는 쪽 또는 받는 쪽 한군데에서만 삭제 처리를 한 경우
            repository.saveAndFlush(message);
        }
        return message;
    }

//    쪽지 일괄 삭제
    public List<Message> deletes(List<Long> seqs, String mode) {
        List<Message> deletes = new ArrayList<>();
        for (Long seq : seqs) {
            Message item = delete(seq, mode);
            if (item != null) {
                deletes.add(item);
            }
        }
        return deletes;
    }
}
