package com.skilljobbie.backend.service.impl;

import com.skilljobbie.backend.dto.StudentDto;
import com.skilljobbie.backend.service.EmailService;
import com.skilljobbie.backend.service.GoogleSheetService;
import com.skilljobbie.backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    private final GoogleSheetService googleSheetService;
    private final EmailService emailService;

    @Autowired
    public StudentServiceImpl(GoogleSheetService googleSheetService, EmailService emailService) {
        this.googleSheetService = googleSheetService;
        this.emailService = emailService;
    }

    @Override
    public StudentDto registerStudent(StudentDto studentDto) {
        
        // 1. Write data to Google Sheets
        try {
            googleSheetService.appendStudentToSheet(studentDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save registration data to Google Sheets: " + e.getMessage(), e);
        }
        
        // 2. Send email notifications
        try {
            emailService.sendCandidateEmail(studentDto);
            emailService.sendHrEmail(studentDto);
        } catch (Exception e) {
            System.err.println("Failed to send email notification: " + e.getMessage());
            throw new RuntimeException("Registration saved to Google Sheets but failed to send email notifications", e);
        }

        return studentDto;
    }
}
