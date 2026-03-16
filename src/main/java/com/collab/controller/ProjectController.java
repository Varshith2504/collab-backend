package com.collab.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.collab.entity.JoinRequest;
import com.collab.entity.Project;
import com.collab.entity.Student;
import com.collab.entity.TeamMember;
import com.collab.repository.JoinRequestRepository;
import com.collab.repository.StudentRepository;
import com.collab.repository.TeamMemberRepository;
import com.collab.service.ProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController {

@Autowired
private ProjectService projectService;

@Autowired
private JoinRequestRepository joinRepo;

@Autowired
private TeamMemberRepository teamRepo;

@Autowired
private StudentRepository studentRepo;





/* GET ALL PROJECTS */

@GetMapping
public List<Project> getProjects(){
    return projectService.getAllProjects();
}


/* CREATE PROJECT */

@PostMapping
public Project createProject(@RequestBody Project project){
    return projectService.saveProject(project);
}


/* DELETE PROJECT */

@DeleteMapping("/{id}")
public void deleteProject(@PathVariable Long id){
    projectService.deleteProject(id);
}


/* SEND JOIN REQUEST */

@PostMapping("/{projectId}/join")
public JoinRequest joinProject(
        @PathVariable Long projectId,
        @RequestBody JoinRequest request){

    request.setProjectId(projectId);

    return joinRepo.save(request);
}


/* GET JOIN REQUESTS */

@GetMapping("/{projectId}/requests")
public List<JoinRequest> getRequests(@PathVariable Long projectId){
    return joinRepo.findByProjectId(projectId);
}


/* ACCEPT REQUEST */

@PostMapping("/{projectId}/accept")
public void acceptRequest(@PathVariable Long projectId,
                          @RequestBody JoinRequest request){

List<JoinRequest> requests = joinRepo.findByProjectId(projectId);

for(JoinRequest jr : requests){

if(jr.getEmail().equals(request.getEmail())){

Project project = projectService.getProjectById(projectId);

if(project.getMembers() < project.getMaxMembers()){

/* increase project members */

project.setMembers(project.getMembers()+1);

projectService.saveProject(project);


/* find student */

Student student = studentRepo.findByEmail(jr.getEmail());


/* add to team members */

TeamMember tm = new TeamMember();

tm.setProjectId(projectId);

tm.setStudentId(student.getId());

teamRepo.save(tm);


/* remove join request */

joinRepo.delete(jr);

}

break;
}

}

}


/* REJECT REQUEST */

@PostMapping("/{projectId}/reject")
public void rejectRequest(@PathVariable Long projectId,
                          @RequestBody JoinRequest request){

List<JoinRequest> requests = joinRepo.findByProjectId(projectId);

for(JoinRequest jr : requests){

if(jr.getEmail().equals(request.getEmail())){

joinRepo.delete(jr);

break;
}

}

}


/* GET TEAM MEMBERS FOR A PROJECT */
@GetMapping("/{projectId}/members")
public List<Student> getMembers(@PathVariable Long projectId) {
    List<TeamMember> teamMembers = teamRepo.findByProjectId(projectId);  // ← teamRepo
    List<Student> students = new ArrayList<>();
    for (TeamMember tm : teamMembers) {
        studentRepo.findById(tm.getStudentId()).ifPresent(students::add);
    }
    return students;
}

@DeleteMapping("/{projectId}/members/{studentId}")
public void removeMember(@PathVariable Long projectId, @PathVariable Long studentId) {
    List<TeamMember> members = teamRepo.findByProjectId(projectId);  // ← teamRepo
    members.stream()
        .filter(tm -> tm.getStudentId().equals(studentId))
        .findFirst()
        .ifPresent(teamRepo::delete);  // ← teamRepo
}
/* LEAVE PROJECT */
@PostMapping("/{projectId}/leave")
public void leaveProject(@PathVariable Long projectId, @RequestBody java.util.Map<String, Long> body) {
    Long studentId = body.get("studentId");
    List<TeamMember> members = teamRepo.findByProjectId(projectId);
    members.stream()
        .filter(tm -> tm.getStudentId().equals(studentId))
        .findFirst()
        .ifPresent(tm -> {
            teamRepo.delete(tm);
            Project project = projectService.getProjectById(projectId);
            project.setMembers(project.getMembers() - 1);
            projectService.saveProject(project);
        });
}
}

