package com.collab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendVerificationEmail(String toEmail, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Verify your CollabHub account");
            helper.setText(
                "<div style='font-family:sans-serif;max-width:480px;margin:auto;padding:2rem;background:#1a1a2e;color:#fff;border-radius:16px;'>" +
                "<h2 style='color:#7c5cfc;'>⚡ CollabHub</h2>" +
                "<h3>Verify your email</h3>" +
                "<p>Thanks for signing up! Click below to verify your account:</p>" +
                "<a href='https://collab-backend-production-a2b8.up.railway.app/students/verify?token=" + token + "' " +
                "style='display:inline-block;padding:0.75rem 1.5rem;background:linear-gradient(135deg,#7c5cfc,#a78bfa);color:#fff;border-radius:10px;text-decoration:none;font-weight:700;margin:1rem 0;'>✅ Verify Email</a>" +
                "<p style='color:#888;font-size:0.85rem;'>This link expires in 24 hours. If you didn't sign up, ignore this email.</p>" +
                "</div>", true
            );
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Email error: " + e.getMessage());
        }
    }
}