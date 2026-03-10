package com.collab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.collab.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{

Student findByEmail(String email);

}