package org.anonymous.message.entities;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MessageBlockId {
    @Id
    private Long seq;

//    차단 컨텐츠 타입(message)
    @Id
    private String type;

//    차단 보낸 사람 이메일
    @Id
    private String email;
}
