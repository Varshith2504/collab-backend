package com.collab.repository;

import com.collab.entity.ProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectFileRepository extends JpaRepository<ProjectFile, Long> {
    List<ProjectFile> findByProjectId(Long projectId);
}