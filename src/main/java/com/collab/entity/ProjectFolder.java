package com.collab.entity;

import jakarta.persistence.*;

@Entity
public class ProjectFolder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long projectId;
    private String folderName;
    private String createdBy;
    private Long parentFolderId;
    public Long getParentFolderId() { return parentFolderId; }
    public void setParentFolderId(Long p) { this.parentFolderId = p; }
    public Long getId() { return id; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long p) { this.projectId = p; }
    public String getFolderName() { return folderName; }
    public void setFolderName(String f) { this.folderName = f; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String c) { this.createdBy = c; }
}