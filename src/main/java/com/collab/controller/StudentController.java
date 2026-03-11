package com.collab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.collab.entity.Student;
import com.collab.service.StudentService;

@RestController
@RequestMapping("/students")
public class StudentController {

@Autowired
private StudentService service;

@PostMapping("/register")
public Student register(@RequestBody Student student){
    return service.saveStudent(student);
}

@PostMapping("/login")
public ResponseEntity<Student> login(@RequestBody Student student) {
    Student found = service.login(student.getEmail(), student.getPassword());
    if (found != null) {
        return ResponseEntity.ok(found);
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
}
@PutMapping("/students/{id}")
public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student updated) {
    return service.getStudentById(id).map(student -> {
        student.setName(updated.getName());
        student.setSkill(updated.getSkill());
        return ResponseEntity.ok(service.saveStudent(student));
    }).orElse(ResponseEntity.notFound().build());
}
}