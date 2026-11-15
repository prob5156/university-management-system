package com.example.ums.unit;

import com.example.ums.model.Admin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdminUnitTest {

    @Test
    @DisplayName("Unit Test 1: Verify Admin entity creation, role, and department")
    void testAdminCreationAndProperties() {
        Admin admin = new Admin("adminuser", "secret123", "System Admin", "IT");

        assertEquals("adminuser", admin.getUsername());
        assertEquals("secret123", admin.getPassword());
        assertEquals("System Admin", admin.getName());
        assertEquals("ROLE_ADMIN", admin.getRole());
        assertEquals("IT", admin.getDepartment());

        // Test setters
        admin.setDepartment("Computer Science");
        assertEquals("Computer Science", admin.getDepartment());
    }
}

