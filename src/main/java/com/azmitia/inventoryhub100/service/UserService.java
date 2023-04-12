package com.azmitia.inventoryhub100.service;

import com.azmitia.inventoryhub100.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO user);
    UserDTO updateUser(String id, String newPassword);
    void deleteUser(String id);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(String id);
}
