package com.example.ums.config;

import com.example.ums.model.Admin;
import com.example.ums.model.Student;
import com.example.ums.model.Teacher;
import com.example.ums.repository.AdminRepository;
import com.example.ums.repository.StudentRepository;
import com.example.ums.repository.TeacherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Seeds the database with default users on first run.
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(AdminRepository adminRepo,
                               TeacherRepository teacherRepo,
                               StudentRepository studentRepo,
                               PasswordEncoder encoder) {
        return args -> {
            if (adminRepo.count() == 0) {
                adminRepo.save(new Admin("admin", encoder.encode("admin123"), "System Admin", "IT"));
                System.out.println("✔ Default admin created  →  username: admin  |  password: admin123");
            }
            if (teacherRepo.count() == 0) {
                teacherRepo.save(new Teacher("teacher", encoder.encode("teacher123"), "Default Teacher", "Computer Science"));
                System.out.println("✔ Default teacher created  →  username: teacher  |  password: teacher123");
            }
            if (studentRepo.count() == 0) {
                studentRepo.save(new Student("student", encoder.encode("student123"), "Default Student", "2207040"));
                System.out.println("✔ Default student created  →  username: student  |  password: student123");
            }
        };
    }
}

