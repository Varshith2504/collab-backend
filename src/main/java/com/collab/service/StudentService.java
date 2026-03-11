package com.collab.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collab.entity.Student;
import com.collab.repository.StudentRepository;

@Service
public class StudentService {

@Autowired
private StudentRepository repo;

public Student saveStudent(Student student){
    return repo.save(student);
}

public Student login(String email,String password){

    Student student = repo.findByEmail(email);

    if(student != null && student.getPassword().equals(password)){
        return student;
    }

    return null;
}

public Optional<Student> getStudentById(Long id) {
    return repo.findById(id);
}

}