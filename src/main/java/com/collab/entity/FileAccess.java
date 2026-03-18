package com.collab.entity;

import jakarta.persistence.*;

@Entity
public class FileAccess {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long projectId;
    private String memberEmail;
    private boolean canEdit;

    public Long getId() { return id; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long p) { this.projectId = p; }
    public String getMemberEmail() { return memberEmail; }
    public void setMemberEmail(String e) { this.memberEmail = e; }
    public boolean isCanEdit() { return canEdit; }
    public void setCanEdit(boolean c) { this.canEdit = c; }
}