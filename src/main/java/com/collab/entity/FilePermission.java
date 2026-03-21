package com.collab.entity;

import jakarta.persistence.*;

@Entity
public class FilePermission {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long fileId;
    private String memberEmail;
    private boolean canEdit;

    public Long getId() { return id; }
    public Long getFileId() { return fileId; }
    public void setFileId(Long f) { this.fileId = f; }
    public String getMemberEmail() { return memberEmail; }
    public void setMemberEmail(String e) { this.memberEmail = e; }
    public boolean isCanEdit() { return canEdit; }
    public void setCanEdit(boolean c) { this.canEdit = c; }
}