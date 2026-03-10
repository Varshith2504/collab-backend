package com.collab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collab.entity.TeamMember;
import com.collab.repository.TeamMemberRepository;

@Service
public class TeamService {

    @Autowired
    private TeamMemberRepository repo;

    // Join a project team
    public TeamMember joinProject(TeamMember t) {
        return repo.save(t);
    }

}