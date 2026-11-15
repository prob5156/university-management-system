package com.example.ums.model;

import jakarta.persistence.*;

/**
 * Admin entity – extends BaseUser.
 */
@Entity
@Table(name = "admins")
public class Admin extends BaseUser {

    @Column(name = "department")
    private String department;

    public Admin() {
        super();
    }

    public Admin(String username, String password, String name, String department) {
        super(username, password, name, "ROLE_ADMIN");
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}

