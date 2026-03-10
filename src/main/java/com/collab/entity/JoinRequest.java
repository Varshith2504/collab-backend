package com.collab.entity;

import jakarta.persistence.*;

@Entity
public class JoinRequest {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private Long projectId;

private String name;

private String email;

private String skills;

private String experience;

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public Long getProjectId() {
	return projectId;
}

public void setProjectId(Long projectId) {
	this.projectId = projectId;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getSkills() {
	return skills;
}

public void setSkills(String skills) {
	this.skills = skills;
}

public String getExperience() {
	return experience;
}

public void setExperience(String experience) {
	this.experience = experience;
}

}