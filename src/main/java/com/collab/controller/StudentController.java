package com.collab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.collab.entity.Student;
import com.collab.service.EmailService;
import com.collab.service.StudentService;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService service;

    @Autowired
    private com.collab.repository.StudentRepository studentRepo;

    @Autowired
    private EmailService emailService;

    // ✅ REGISTER WITH EMAIL VERIFICATION
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Student student) {

        if (studentRepo.findByEmail(student.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        String token = java.util.UUID.randomUUID().toString();
        student.setVerificationToken(token);
        student.setVerified(false);

        service.saveStudent(student);

        emailService.sendVerificationEmail(student.getEmail(), token);

        return ResponseEntity.ok("verification_sent");
    }

    // ✅ EMAIL VERIFY
    @GetMapping("/verify")
    public void verifyEmail(@RequestParam String token,
                            jakarta.servlet.http.HttpServletResponse response) throws Exception {

        studentRepo.findAll().stream()
            .filter(s -> token.equals(s.getVerificationToken()))
            .findFirst()
            .ifPresent(s -> {
                s.setVerified(true);
                s.setVerificationToken(null);
                studentRepo.save(s);
            });

        response.sendRedirect("https://collab-frontend-liart.vercel.app?verified=true");
    }

    // ✅ LOGIN (ONLY VERIFIED USERS)
    @PostMapping("/login")
    public ResponseEntity<Student> login(@RequestBody Student student) {

        Student found = service.login(student.getEmail(), student.getPassword());

        if (found == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (!found.isVerified())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok(found);
    }

    // ✅ UPDATE PROFILE
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