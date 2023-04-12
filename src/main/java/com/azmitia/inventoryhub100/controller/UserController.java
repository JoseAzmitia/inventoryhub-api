package com.azmitia.inventoryhub100.controller;

import com.azmitia.inventoryhub100.dto.UserDTO;
import com.azmitia.inventoryhub100.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping(value = "/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user) {
        UserDTO createdUser = service.createUser(user);
        if (createdUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value="/{id}/password")
    public ResponseEntity<UserDTO> updateUserPassword(@PathVariable String id, @RequestBody String newPassword) {
        UserDTO updatedUser = service.updateUser(id, newPassword);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value="/list")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = service.getAllUsers();
        if (users != null) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        UserDTO user = service.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
