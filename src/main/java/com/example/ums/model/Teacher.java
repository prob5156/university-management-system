package com.example.ums.model;

import jakarta.persistence.*;

/**
 * Teacher entity – extends BaseUser.
 */
@Entity
@Table(name = "teachers")
public class Teacher extends BaseUser {

    @Column(name = "subject")
    private String subject;

    public Teacher() {
        super();
    }

    public Teacher(String username, String password, String name, String subject) {
        super(username, password, name, "ROLE_TEACHER");
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}

