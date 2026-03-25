package com.collab.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.collab.entity.Student;
import com.collab.repository.StudentRepository;

@Service
public class StudentService {

private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

@Autowired
private StudentRepository repo;

@Autowired
private PasswordEncoder passwordEncoder;

public Student saveStudent(Student student){
    if (student.getPassword() != null && !isHashed(student.getPassword())) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
    }
    if (student.getRole() == null) {
        if (repo.count() == 0) {
            student.setRole("ROLE_ADMIN");
        } else {
            student.setRole("ROLE_USER");
        }
    }
    return repo.save(student);
}

private boolean isHashed(String password) {
    // Check if password matches standard BCrypt prefixes ($2a$, $2b$, or $2y$)
    return password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$");
}

public Student login(String email,String password){
    logger.info("Attempting login for email: {}", email);

    Student student = repo.findByEmail(email);

    if(student == null){
        logger.warn("Login failed: User not found with email {}", email);
        return null;
    }

    // Try standard BCrypt matching
    if(isHashed(student.getPassword())){
        if(passwordEncoder.matches(password, student.getPassword())){
            logger.info("Login successful for email: {}", email);
            return student;
        } else {
            logger.warn("Login failed: Incorrect password for email {}", email);
        }
    } else {
        // Graceful migration: handle legacy plain text passwords
        if(student.getPassword().equals(password)){
            logger.info("Login successful (legacy) for email: {}. Migrating to hashed password.", email);
            // Re-hash and update for next time
            student.setPassword(passwordEncoder.encode(password));
            return repo.save(student);
        } else {
            logger.warn("Login failed: Incorrect legacy password for email {}", email);
        }
    }

    return null;
}

public Optional<Student> getStudentById(Long id) {
    return repo.findById(id);
}

}