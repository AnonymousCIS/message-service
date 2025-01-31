package org.anonymous.message.repositories;

import org.anonymous.message.entities.MessageBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MessageBlockRepository extends JpaRepository<MessageBlock, Long>, QuerydslPredicateExecutor<MessageBlock> {
}
