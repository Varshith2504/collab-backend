package com.collab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.collab.entity.TeamMember;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findByProjectId(Long projectId);
    com.collab.entity.TeamMember findByStudentIdAndProjectId(Long studentId, Long projectId);
}