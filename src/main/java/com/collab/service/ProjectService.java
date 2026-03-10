package com.collab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collab.entity.Project;
import com.collab.repository.ProjectRepository;

@Service
public class ProjectService {

@Autowired
private ProjectRepository repo;

public List<Project> getAllProjects(){
    return repo.findAll();
}

public Project saveProject(Project project){
    return repo.save(project);
}

public void deleteProject(Long id){
    repo.deleteById(id);
}

public Project getProjectById(Long id) {
    return repo.findById(id).orElse(null);
}


}