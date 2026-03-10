package com.collab.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.collab.entity.JoinRequest;

public interface JoinRequestRepository extends JpaRepository<JoinRequest,Long>{

List<JoinRequest> findByProjectId(Long projectId);

}