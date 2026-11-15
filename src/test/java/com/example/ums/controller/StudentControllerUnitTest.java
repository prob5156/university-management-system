package com.example.ums.controller;

import com.example.ums.model.Student;
import com.example.ums.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentControllerUnitTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private StudentController studentController;

    @BeforeEach
    void setUp() {
        studentController = new StudentController(studentRepository, passwordEncoder);
    }

    @Test
    @DisplayName("Unit Test 6: Create student encodes password, sets ROLE_STUDENT and saves to repository")
    void testCreateStudent_EncodesPasswordAndSaves() {
        Student inputStudent = new Student("student1", "plainPassword", "Student One", "2207040");

        when(passwordEncoder.encode("plainPassword")).thenReturn("hashedPassword");
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student s = invocation.getArgument(0);
            s.setId(1L);  // Simulate JPA auto-generating the ID
            return s;
        });

        ResponseEntity<Map<String, Object>> response = studentController.createStudent(inputStudent);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("student1", response.getBody().get("username"));
        assertEquals("ROLE_STUDENT", response.getBody().get("role"));

        // Verify password was hashed and saved
        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(captor.capture());
        assertEquals("hashedPassword", captor.getValue().getPassword());
        assertEquals("ROLE_STUDENT", captor.getValue().getRole());
    }
}
