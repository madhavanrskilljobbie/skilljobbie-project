package com.skilljobbie.backend.service;

import com.skilljobbie.backend.dto.StudentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${skilljobbie.hr.email}")
    private String hrEmail;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendCandidateEmail(StudentDto student) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(student.getEmail());
        message.setSubject("SkillJobbie Registration Successful");
        message.setText("Hi " + student.getName() + ",\n\n" +
                "You have successfully registered in SkillJobbie.\n" +
                "Our team will contact you soon.\n\n" +
                "Best Regards,\nSkillJobbie Team");
        
        mailSender.send(message);
    }

    public void sendHrEmail(StudentDto student) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(hrEmail);
        message.setSubject("New Student Registration");
        message.setText("A new student has registered.\n\n" +
                "Registration Details:\n" +
                "Name: " + student.getName() + "\n" +
                "Email: " + student.getEmail() + "\n" +
                "Phone: " + student.getPhone() + "\n" +
                "Course: " + student.getCourse());
        
        mailSender.send(message);
    }
}
