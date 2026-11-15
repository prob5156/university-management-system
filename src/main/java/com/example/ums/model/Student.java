package com.example.ums.model;

import jakarta.persistence.*;

/**
 * Student entity – extends BaseUser.
 */
@Entity
@Table(name = "students")
public class Student extends BaseUser {

    @Column(name = "student_id", unique = true)
    private String studentId;

    public Student() {
        super();
    }

    public Student(String username, String password, String name, String studentId) {
        super(username, password, name, "ROLE_STUDENT");
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}

