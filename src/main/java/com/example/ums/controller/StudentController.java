package com.example.ums.controller;

import com.example.ums.model.Student;
import com.example.ums.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for Student CRUD operations.
 * Accessible by ROLE_ADMIN and ROLE_STUDENT.
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentController(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------- CREATE ----------
    @PostMapping
    public ResponseEntity<Map<String, Object>> createStudent(@RequestBody Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setRole("ROLE_STUDENT");
        Student saved = studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    // ---------- READ ALL ----------
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllStudents() {
        List<Map<String, Object>> list = studentRepository.findAll()
                .stream().map(this::toResponse).toList();
        return ResponseEntity.ok(list);
    }

    // ---------- READ ONE ----------
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(student -> ResponseEntity.ok(toResponse(student)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------- UPDATE ----------
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody Student updated) {
        return studentRepository.findById(id).map(student -> {
            student.setName(updated.getName());
            student.setUsername(updated.getUsername());
            student.setStudentId(updated.getStudentId());
            if (updated.getPassword() != null && !updated.getPassword().isBlank()) {
                student.setPassword(passwordEncoder.encode(updated.getPassword()));
            }
            studentRepository.save(student);
            return ResponseEntity.ok(toResponse(student));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ---------- DELETE ----------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        studentRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Student deleted successfully"));
    }

    // ---------- Helper ----------
    private Map<String, Object> toResponse(Student s) {
        return Map.of(
                "id", s.getId(),
                "username", s.getUsername(),
                "name", s.getName(),
                "role", s.getRole(),
                "studentId", s.getStudentId() != null ? s.getStudentId() : ""
        );
    }
}

