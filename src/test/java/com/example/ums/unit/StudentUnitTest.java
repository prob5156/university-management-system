package com.example.ums.unit;

import com.example.ums.model.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentUnitTest {

    @Test
    @DisplayName("Unit Test 3: Verify Student entity creation, role, and studentId")
    void testStudentCreationAndProperties() {
        Student student = new Student("studentuser", "secret123", "Prottoy", "2207040");

        assertEquals("studentuser", student.getUsername());
        assertEquals("secret123", student.getPassword());
        assertEquals("Prottoy", student.getName());
        assertEquals("ROLE_STUDENT", student.getRole());
        assertEquals("2207040", student.getStudentId());

        // Test setters
        student.setName("Prottoy Updated");
        assertEquals("Prottoy Updated", student.getName());
        student.setStudentId("2207099");
        assertEquals("2207099", student.getStudentId());
    }
}

