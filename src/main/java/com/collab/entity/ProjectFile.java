package com.collab.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ProjectFile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long projectId;
    private String fileName;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String language;
    private String uploadedBy;
    private LocalDateTime uploadedAt;

    @PrePersist
    public void prePersist() { this.uploadedAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long p) { this.projectId = p; }
    public String getFileName() { return fileName; }
    public void setFileName(String f) { this.fileName = f; }
    public String getContent() { return content; }
    public void setContent(String c) { this.content = c; }
    public String getLanguage() { return language; }
    public void setLanguage(String l) { this.language = l; }
    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String u) { this.uploadedBy = u; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime t) { this.uploadedAt = t; }
}