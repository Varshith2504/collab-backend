package com.collab.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.collab.entity.FileAccess;
import com.collab.entity.FilePermission;
import com.collab.entity.JoinRequest;
import com.collab.entity.Project;
import com.collab.entity.ProjectFile;
import com.collab.entity.ProjectFolder;
import com.collab.entity.Student;
import com.collab.entity.TeamMember;
import com.collab.repository.FileAccessRepository;
import com.collab.repository.FilePermissionRepository;
import com.collab.repository.JoinRequestRepository;
import com.collab.repository.ProjectFileRepository;
import com.collab.repository.ProjectFolderRepository;
import com.collab.repository.ProjectRepository;
import com.collab.repository.StudentRepository;
import com.collab.repository.TeamMemberRepository;
import com.collab.service.ProjectService;
import org.springframework.http.ResponseEntity;

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

@Autowired 
private ProjectRepository projectRepo;

@Autowired 
private ProjectFileRepository fileRepo;
@Autowired 
private FileAccessRepository fileAccessRepo;

@Autowired
private com.collab.repository.NotificationRepository notificationRepo;



/* GET ALL PROJECTS */

@GetMapping
public List<Project> getProjects(){
    return projectService.getAllProjects();
}


/* CREATE PROJECT */

@PostMapping
public Project createProject(@RequestBody Project project){
    if(project.getStatus() == null) project.setStatus("Active");
    return projectService.saveProject(project);
}


/* UPDATE PROJECT */

@PostMapping("/{id}")
public Project updateProject(@PathVariable Long id, @RequestBody Project project){
    Project existing = projectService.getProjectById(id);
    if(existing != null){
        project.setId(id);
        return projectService.saveProject(project);
    }
    return null;
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
    JoinRequest saved = joinRepo.save(request);

    /* Notify owner */
    Project p = projectService.getProjectById(projectId);
    if (p != null) {
        com.collab.entity.Notification n = new com.collab.entity.Notification();
        n.setRecipientEmail(p.getOwner());
        n.setMessage("New join request for project: " + p.getName());
        n.setRead(false);
        n.setCreatedAt(java.time.LocalDateTime.now());
        notificationRepo.save(n);
    }

    return saved;
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

/* Notify student */
com.collab.entity.Notification n = new com.collab.entity.Notification();
n.setRecipientEmail(jr.getEmail());
n.setMessage("Your join request for project " + project.getName() + " was accepted!");
n.setRead(false);
n.setCreatedAt(java.time.LocalDateTime.now());
notificationRepo.save(n);

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

@Autowired
private com.collab.repository.MessageRepository messageRepo;

// ── Chat ──
@GetMapping("/{id}/messages")
public List<com.collab.entity.Message> getMessages(@PathVariable Long id) {
    return messageRepo.findByProjectIdOrderBySentAtAsc(id);
}

@PostMapping("/{id}/messages")
public com.collab.entity.Message sendMessage(@PathVariable Long id,
        @RequestBody com.collab.entity.Message msg) {
    msg.setProjectId(id);
    return messageRepo.save(msg);
}

// ── Resource URL ──
@PutMapping("/{id}/resource")
public ResponseEntity<Project> updateResource(@PathVariable Long id,
        @RequestBody java.util.Map<String, String> body) {
    return projectRepo.findById(id).map(p -> {
        p.setResourceUrl(body.get("resourceUrl"));
        return ResponseEntity.ok(projectRepo.save(p));
    }).orElse(ResponseEntity.notFound().build());
}
@GetMapping("/{id}/files")
public ResponseEntity<List<ProjectFile>> getFiles(@PathVariable Long id) {
    return ResponseEntity.ok(fileRepo.findByProjectId(id));
}

@PostMapping("/{id}/files")
public ResponseEntity<ProjectFile> addFile(@PathVariable Long id,
        @RequestBody ProjectFile file) {
    file.setProjectId(id);
    return ResponseEntity.ok(fileRepo.save(file));
}

@PutMapping("/{id}/files/{fileId}")
public ResponseEntity<ProjectFile> updateFile(@PathVariable Long id,
        @PathVariable Long fileId, @RequestBody ProjectFile updated) {
    return fileRepo.findById(fileId).map(f -> {
        f.setContent(updated.getContent());
        f.setFileName(updated.getFileName());
        f.setLanguage(updated.getLanguage());
        return ResponseEntity.ok(fileRepo.save(f));
    }).orElse(ResponseEntity.notFound().build());
}

@DeleteMapping("/{id}/files/{fileId}")
public ResponseEntity<Void> deleteFile(@PathVariable Long id,
        @PathVariable Long fileId) {
    fileRepo.deleteById(fileId);
    return ResponseEntity.ok().build();
}

// ── File Access ──
@GetMapping("/{id}/access")
public ResponseEntity<List<FileAccess>> getAccess(@PathVariable Long id) {
    return ResponseEntity.ok(fileAccessRepo.findByProjectId(id));
}

@PostMapping("/{id}/access")
public ResponseEntity<FileAccess> grantAccess(@PathVariable Long id,
        @RequestBody FileAccess access) {
    // Update if exists
    fileAccessRepo.findByProjectIdAndMemberEmail(id, access.getMemberEmail())
        .ifPresent(existing -> fileAccessRepo.deleteById(existing.getId()));
    access.setProjectId(id);
    return ResponseEntity.ok(fileAccessRepo.save(access));
}
@Autowired private ProjectFolderRepository folderRepo;

@GetMapping("/{id}/folders")
public ResponseEntity<List<ProjectFolder>> getFolders(@PathVariable Long id) {
    return ResponseEntity.ok(folderRepo.findByProjectId(id));
}

@PostMapping("/{id}/folders")
public ResponseEntity<ProjectFolder> addFolder(@PathVariable Long id, @RequestBody ProjectFolder folder) {
    folder.setProjectId(id);
    return ResponseEntity.ok(folderRepo.save(folder));
}

@DeleteMapping("/{id}/folders/{folderId}")
public ResponseEntity<Void> deleteFolder(@PathVariable Long id, @PathVariable Long folderId) {
    folderRepo.deleteById(folderId);
    // also delete files in this folder
    fileRepo.findByProjectId(id).stream()
        .filter(f -> folderId.equals(f.getFolderId()))
        .forEach(f -> fileRepo.deleteById(f.getId()));
    return ResponseEntity.ok().build();
}

@Autowired private FilePermissionRepository filePermRepo;

//Per-file permissions
@GetMapping("/{id}/files/{fileId}/permissions")
public ResponseEntity<List<FilePermission>> getFilePermissions(
     @PathVariable Long id, @PathVariable Long fileId) {
 return ResponseEntity.ok(filePermRepo.findByFileId(fileId));
}

@PostMapping("/{id}/files/{fileId}/permissions")
public ResponseEntity<FilePermission> setFilePermission(
     @PathVariable Long id, @PathVariable Long fileId,
     @RequestBody FilePermission perm) {
 filePermRepo.findByFileIdAndMemberEmail(fileId, perm.getMemberEmail())
     .ifPresent(existing -> filePermRepo.deleteById(existing.getId()));
 perm.setFileId(fileId);
 return ResponseEntity.ok(filePermRepo.save(perm));
}

@DeleteMapping("/{id}/files/{fileId}/permissions/{email}")
public ResponseEntity<Void> removeFilePermission(
     @PathVariable Long id, @PathVariable Long fileId,
     @PathVariable String email) {
 filePermRepo.findByFileIdAndMemberEmail(fileId, email)
     .ifPresent(p -> filePermRepo.deleteById(p.getId()));
 return ResponseEntity.ok().build();
}
}

