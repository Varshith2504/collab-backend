package com.collab.repository;

import com.collab.entity.FileAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FileAccessRepository extends JpaRepository<FileAccess, Long> {
    List<FileAccess> findByProjectId(Long projectId);
    Optional<FileAccess> findByProjectIdAndMemberEmail(Long projectId, String email);
}