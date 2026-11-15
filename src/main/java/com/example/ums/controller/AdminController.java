package com.example.ums.controller;

import com.example.ums.model.Admin;
import com.example.ums.repository.AdminRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for Admin CRUD operations.
 * Accessible only by users with ROLE_ADMIN.
 */
@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------- CREATE ----------
    @PostMapping
    public ResponseEntity<Map<String, Object>> createAdmin(@RequestBody Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRole("ROLE_ADMIN");
        Admin saved = adminRepository.save(admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    // ---------- READ ALL ----------
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllAdmins() {
        List<Map<String, Object>> list = adminRepository.findAll()
                .stream().map(this::toResponse).toList();
        return ResponseEntity.ok(list);
    }

    // ---------- READ ONE ----------
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable Long id) {
        return adminRepository.findById(id)
                .map(admin -> ResponseEntity.ok(toResponse(admin)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------- UPDATE ----------
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Long id, @RequestBody Admin updated) {
        return adminRepository.findById(id).map(admin -> {
            admin.setName(updated.getName());
            admin.setUsername(updated.getUsername());
            admin.setDepartment(updated.getDepartment());
            if (updated.getPassword() != null && !updated.getPassword().isBlank()) {
                admin.setPassword(passwordEncoder.encode(updated.getPassword()));
            }
            adminRepository.save(admin);
            return ResponseEntity.ok(toResponse(admin));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ---------- DELETE ----------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        if (!adminRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        adminRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Admin deleted successfully"));
    }

    // ---------- Helper ----------
    private Map<String, Object> toResponse(Admin a) {
        return Map.of(
                "id", a.getId(),
                "username", a.getUsername(),
                "name", a.getName(),
                "role", a.getRole(),
                "department", a.getDepartment() != null ? a.getDepartment() : ""
        );
    }
}

