package org.anonymous.message.repositories;

import org.anonymous.message.entities.Message;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>, QuerydslPredicateExecutor<Message> {
    List<Message> findAllByCreatedBy(String email);
}
