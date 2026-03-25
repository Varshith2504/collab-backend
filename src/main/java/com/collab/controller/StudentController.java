package com.collab.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.collab.entity.Student;
import com.collab.service.StudentService;
import com.collab.security.JwtUtil;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService service;

    @Autowired
    private com.collab.repository.StudentRepository studentRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Student student) {
        if (studentRepo.findByEmail(student.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already registered");
        }
        Student saved = service.saveStudent(student);
        String token = jwtUtil.generateToken(saved.getEmail());
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("student", saved);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Student student) {
        Student found = service.login(student.getEmail(), student.getPassword());
        if (found == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        
        String token = jwtUtil.generateToken(found.getEmail());
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("student", found);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id,
                                                 @RequestBody Student updated) {
        return service.getStudentById(id).map(student -> {
            student.setName(updated.getName());
            student.setSkill(updated.getSkill());
            return ResponseEntity.ok(service.saveStudent(student));
        }).orElse(ResponseEntity.notFound().build());
    }
}