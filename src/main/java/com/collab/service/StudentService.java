package com.collab.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.collab.entity.Student;
import com.collab.repository.StudentRepository;

@Service
public class StudentService {

@Autowired
private StudentRepository repo;

@Autowired
private PasswordEncoder passwordEncoder;

public Student saveStudent(Student student){
    if (student.getPassword() != null && !student.getPassword().startsWith("$2a$")) {
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

public Student login(String email,String password){

    Student student = repo.findByEmail(email);

    if(student != null && passwordEncoder.matches(password, student.getPassword())){
        return student;
    }

    return null;
}

public Optional<Student> getStudentById(Long id) {
    return repo.findById(id);
}

}