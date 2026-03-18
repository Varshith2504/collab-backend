package com.collab.repository;

import com.collab.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByProjectIdOrderBySentAtAsc(Long projectId);
}