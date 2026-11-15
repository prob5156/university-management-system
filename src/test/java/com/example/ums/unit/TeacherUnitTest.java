package com.example.ums.unit;

import com.example.ums.model.Teacher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherUnitTest {

    @Test
    @DisplayName("Unit Test 2: Verify Teacher entity creation, role, and subject")
    void testTeacherCreationAndProperties() {
        Teacher teacher = new Teacher("teacheruser", "secret123", "Dr. Rahman", "Software Engineering");

        assertEquals("teacheruser", teacher.getUsername());
        assertEquals("secret123", teacher.getPassword());
        assertEquals("Dr. Rahman", teacher.getName());
        assertEquals("ROLE_TEACHER", teacher.getRole());
        assertEquals("Software Engineering", teacher.getSubject());

        // Test setters
        teacher.setSubject("Data Structures");
        assertEquals("Data Structures", teacher.getSubject());
    }
}

