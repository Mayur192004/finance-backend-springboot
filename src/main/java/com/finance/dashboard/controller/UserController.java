package com.finance.dashboard.controller;

import com.finance.dashboard.dto.UserResponse;
import com.finance.dashboard.entity.Role;
import com.finance.dashboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<UserResponse> updateUserRole(@PathVariable Long id, @RequestParam Role role) {
        return ResponseEntity.ok(userService.updateUserRole(id, role));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<UserResponse> toggleUserStatus(@PathVariable Long id, @RequestParam boolean active) {
        return ResponseEntity.ok(userService.toggleUserStatus(id, active));
    }
}
