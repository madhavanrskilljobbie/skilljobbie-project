package com.skilljobbie.backend.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.skilljobbie.backend.dto.StudentDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

@Service
public class GoogleSheetService {

    @Value("${google.sheets.spreadsheet.id}")
    private String spreadsheetId;

    @Value("${google.sheets.credentials.file.path}")
    private String credentialsFilePath;

    private static final String APPLICATION_NAME = "SkillJobbie Registration";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private Sheets getSheetsService() throws Exception {
        InputStream in;
        if (credentialsFilePath.startsWith("classpath:")) {
            String path = credentialsFilePath.substring("classpath:".length());
            in = this.getClass().getResourceAsStream("/" + path);
            if (in == null) {
                // Fallback attempt
                in = this.getClass().getClassLoader().getResourceAsStream(path);
            }
        } else {
            in = new FileInputStream(ResourceUtils.getFile(credentialsFilePath));
        }

        if (in == null) {
            throw new RuntimeException("Credentials file not found: " + credentialsFilePath);
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                .createScoped(Collections.singletonList(SheetsScopes.SPREADSHEETS));

        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), 
                JSON_FACTORY, 
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public void appendStudentToSheet(StudentDto student) {
        try {
            Sheets sheetsService = getSheetsService();
            ValueRange body = new ValueRange()
                    .setValues(Collections.singletonList(
                            Arrays.asList(student.getName(), student.getEmail(), student.getPhone(), student.getCourse())
                    ));

            sheetsService.spreadsheets().values()
                    .append(spreadsheetId, "Sheet1!A:D", body)
                    .setValueInputOption("USER_ENTERED")
                    .execute();
        } catch (Exception e) {
            throw new RuntimeException("Failed to write to Google Sheets: " + e.getMessage(), e);
        }
    }
}
