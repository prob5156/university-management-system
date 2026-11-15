package com.example.ums.controller;

import com.example.ums.model.Teacher;
import com.example.ums.repository.TeacherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for Teacher CRUD operations.
 * Accessible by ROLE_ADMIN and ROLE_TEACHER.
 */
@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    public TeacherController(TeacherRepository teacherRepository, PasswordEncoder passwordEncoder) {
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------- CREATE ----------
    @PostMapping
    public ResponseEntity<Map<String, Object>> createTeacher(@RequestBody Teacher teacher) {
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacher.setRole("ROLE_TEACHER");
        Teacher saved = teacherRepository.save(teacher);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    // ---------- READ ALL ----------
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllTeachers() {
        List<Map<String, Object>> list = teacherRepository.findAll()
                .stream().map(this::toResponse).toList();
        return ResponseEntity.ok(list);
    }

    // ---------- READ ONE ----------
    @GetMapping("/{id}")
    public ResponseEntity<?> getTeacherById(@PathVariable Long id) {
        return teacherRepository.findById(id)
                .map(teacher -> ResponseEntity.ok(toResponse(teacher)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------- UPDATE ----------
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTeacher(@PathVariable Long id, @RequestBody Teacher updated) {
        return teacherRepository.findById(id).map(teacher -> {
            teacher.setName(updated.getName());
            teacher.setUsername(updated.getUsername());
            teacher.setSubject(updated.getSubject());
            if (updated.getPassword() != null && !updated.getPassword().isBlank()) {
                teacher.setPassword(passwordEncoder.encode(updated.getPassword()));
            }
            teacherRepository.save(teacher);
            return ResponseEntity.ok(toResponse(teacher));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ---------- DELETE ----------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Long id) {
        if (!teacherRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        teacherRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Teacher deleted successfully"));
    }

    // ---------- Helper ----------
    private Map<String, Object> toResponse(Teacher t) {
        return Map.of(
                "id", t.getId(),
                "username", t.getUsername(),
                "name", t.getName(),
                "role", t.getRole(),
                "subject", t.getSubject() != null ? t.getSubject() : ""
        );
    }
}

