package com.collab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.collab.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}