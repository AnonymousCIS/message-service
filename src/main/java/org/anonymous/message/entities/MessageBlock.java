package org.anonymous.message.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.anonymous.global.entities.BaseMemberEntity;
import org.anonymous.message.constants.MessageStatus;

@Data
@Entity
@IdClass(MessageBlockId.class)
public class MessageBlock extends BaseMemberEntity {
    @Id
    private Long seq;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @Id
    @Column(nullable = false)
    private String type;

    @Id
    @Column(nullable = false)
    private String email;


}
