package com.example.ums.integration;

import com.example.ums.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UniversityManagementIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    @DisplayName("Integration Test 1: Admin can successfully access /api/admins (HTTP 200 OK)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminAccessAuthorized() throws Exception {
        mockMvc.perform(get("/api/admins")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Integration Test 2: Student is forbidden from accessing /api/admins (HTTP 403 Forbidden)")
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void testStudentAccessForbiddenOnAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/admins")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Integration Test 3: End-to-End create student via API, verifies DB persistence (HTTP 201 Created)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateStudentEndToEndPersistence() throws Exception {
        String studentJson = """
                {
                    "username": "integ_student",
                    "password": "integPass123",
                    "name": "Integration Student",
                    "studentId": "2207999"
                }
                """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("integ_student"))
                .andExpect(jsonPath("$.studentId").value("2207999"))
                .andExpect(jsonPath("$.role").value("ROLE_STUDENT"));

        // Verify that the student is actually persisted in the database
        boolean exists = studentRepository.findAll().stream()
                .anyMatch(s -> "integ_student".equals(s.getUsername()));
        assertTrue(exists, "Student should be saved in the database");
    }
}

