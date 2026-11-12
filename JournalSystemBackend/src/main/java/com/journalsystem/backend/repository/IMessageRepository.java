
package com.journalsystem.backend.repository;

import com.journalsystem.backend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IMessageRepository extends JpaRepository<Message, Long>{

    @Query("""
        SELECT m FROM Message m
        WHERE (m.sender.id = :userId AND m.receiver.id = :otherUserId)
           OR (m.sender.id = :otherUserId AND m.receiver.id = :userId)
        ORDER BY m.timestamp ASC
    """)
    List<Message> findConversation(
            @Param("userId") Long userId,
            @Param("otherUserId") Long otherUserId
    );
}
