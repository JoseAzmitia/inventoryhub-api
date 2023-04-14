package com.azmitia.inventoryhub100.controller;

import com.azmitia.inventoryhub100.dto.UserDTO;
import com.azmitia.inventoryhub100.service.FirebaseAuthenticationService;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private FirebaseAuthenticationService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserDTO user) {
        try {
            Map<String, Object> authResponse = authService.authenticateUser(user);
            String customToken = (String) authResponse.get("customToken");
            String uid = (String) authResponse.get("uid");
            Map<String, String> response = new HashMap<>();
            response.put("token", customToken);
            response.put("uid", uid);
            return ResponseEntity.ok(response);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid credentials"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO user) {
        try {
            authService.createUser(user);
            return ResponseEntity.ok("User created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user: " + e.getMessage());
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String email) {
        try {
            authService.resetPassword(email);
            return ResponseEntity.ok("Password reset link sent to email");
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending password reset link: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody Map<String, String> payload) {
        try {
            authService.logout(payload.get("uid"));
            Map<String, String> response = new HashMap<>();
            response.put("message", "User logged out successfully");
            return ResponseEntity.ok(response);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getMessage()));
        }
    }


}
