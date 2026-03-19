package com.collab.repository;

import com.collab.entity.ProjectFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectFolderRepository extends JpaRepository<ProjectFolder, Long> {
    List<ProjectFolder> findByProjectId(Long projectId);
}