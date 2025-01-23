package org.anonymous.message.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.anonymous.global.entities.BaseEntity;
import org.anonymous.global.entities.BaseMemberEntity;
import org.anonymous.message.constants.MessageStatus;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Message extends BaseMemberEntity {

    @Id @GeneratedValue
    private Long seq;

    private boolean notice; // 공지

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private MessageStatus status;


    private String senderEmail; // 보낸사람 이메일


    private String senderName; // 보낸 사람 이름


    private String receiverEmail; //받는 사람 이메일


    private String receiverName; // 받는 사람 이름

    @Column(length = 150, nullable = false)
    private String subject; // 제목

    @Lob
    @Column(nullable = false)
    private String content; // 내용

    @Transient
    private boolean received;

    @Transient
    private boolean deletable; // 삭제 가능 여부


    private boolean deletedBySender; // 보내는 쪽에서 쪽지를 삭제한 경우


    private boolean deletedByReceiver; // 받는 쪽에서 쪽지를 삭제한 경우

}
