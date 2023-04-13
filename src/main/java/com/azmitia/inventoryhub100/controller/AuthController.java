package com.azmitia.inventoryhub100.controller;

import com.azmitia.inventoryhub100.dto.UserDTO;
import com.azmitia.inventoryhub100.service.FirebaseAuthenticationService;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            String idToken = authService.authenticateUser(user);
            Map<String, String> response = new HashMap<>();
            response.put("token", idToken);
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

}
