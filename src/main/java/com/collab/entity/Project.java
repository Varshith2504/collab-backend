package com.collab.entity;

import jakarta.persistence.*;

@Entity
public class Project {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String name;
private String description;
private String skills;
private int maxMembers;
private int members;
private String owner;
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getSkills() {
	return skills;
}
public void setSkills(String skills) {
	this.skills = skills;
}
public int getMaxMembers() {
	return maxMembers;
}
public void setMaxMembers(int maxMembers) {
	this.maxMembers = maxMembers;
}
public int getMembers() {
	return members;
}
public void setMembers(int members) {
	this.members = members;
}
public String getOwner() {
	return owner;
}
public void setOwner(String owner) {
	this.owner = owner;
}

}