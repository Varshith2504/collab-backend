package com.collab.controller;

import com.collab.entity.Notification;
import com.collab.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationRepository repo;

    @GetMapping("/{email}")
    public List<Notification> getNotifications(@PathVariable String email) {
        return repo.findByRecipientEmailOrderByCreatedAtDesc(email);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id) {
        return repo.findById(id).map(n -> {
            n.setRead(true);
            return ResponseEntity.ok(repo.save(n));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
