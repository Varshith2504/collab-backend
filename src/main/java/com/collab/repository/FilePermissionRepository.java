package com.collab.repository;

import com.collab.entity.FilePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FilePermissionRepository extends JpaRepository<FilePermission, Long> {
    List<FilePermission> findByFileId(Long fileId);
    Optional<FilePermission> findByFileIdAndMemberEmail(Long fileId, String email);
}