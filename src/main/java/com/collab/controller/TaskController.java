package com.collab.controller;

import com.collab.entity.Task;
import com.collab.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository repo;

    @GetMapping("/project/{projectId}")
    public List<Task> getTasksByProject(@PathVariable Long projectId) {
        return repo.findByProjectId(projectId);
    }

    @GetMapping("/student/{email}")
    public List<Task> getTasksByStudent(@PathVariable String email) {
        return repo.findByAssignedTo(email);
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        if (task.getStatus() == null) task.setStatus("Pending");
        return repo.save(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updated) {
        return repo.findById(id).map(task -> {
            task.setTitle(updated.getTitle());
            task.setDescription(updated.getDescription());
            task.setStatus(updated.getStatus());
            task.setAssignedTo(updated.getAssignedTo());
            task.setDueDate(updated.getDueDate());
            return ResponseEntity.ok(repo.save(task));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
