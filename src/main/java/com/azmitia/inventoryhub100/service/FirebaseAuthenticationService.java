package com.azmitia.inventoryhub100.service;

import com.azmitia.inventoryhub100.dto.UserDTO;
import com.google.firebase.ErrorCode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.*;
import firebase.FirebaseInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseAuthenticationService {
    private final FirebaseAuth firebaseAuth;
    private final UserService userService;

    @Autowired
    public FirebaseAuthenticationService(FirebaseInitializer firebaseInitializer, UserService userService) throws IOException {
        this.userService = userService;
        this.firebaseAuth = FirebaseAuth.getInstance(FirebaseApp.getInstance());
    }


    public Map<String, Object> authenticateUser(UserDTO user) throws FirebaseAuthException {
        UserRecord userRecord = firebaseAuth.getUserByEmail(user.getEmail());
        String uid = userRecord.getUid();
        String customToken = firebaseAuth.createCustomToken(uid);
        System.out.println(customToken);
        UserDTO userFirestore = userService.getUserByEmail(user.getEmail());
        Map<String, Object> response = new HashMap<>();
        response.put("uid", uid);
        response.put("token", customToken);
        response.put("userId", userFirestore.getId());
        response.put("email", userFirestore.getEmail());
        return response;
    }

    public String createUser(UserDTO user) throws FirebaseAuthException {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(user.getEmail())
                .setPassword(user.getPassword());

        UserRecord userRecord = firebaseAuth.createUser(request);

        UserDTO newUser = userService.createUser(new UserDTO(user.getEmail(), user.getPassword()));
        return userRecord.getUid();
    }

    public void resetPassword(String email) throws FirebaseAuthException {
        String link = firebaseAuth.generatePasswordResetLink(email);
        System.out.println("Link: " + link);
    }

    public void logout(String uid) throws FirebaseAuthException {
        UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
        if (userRecord != null) {
            FirebaseAuth.getInstance().revokeRefreshTokens(uid);
            System.out.println("User logged out successfully");
        } else {
            throw new FirebaseAuthException(ErrorCode.NOT_FOUND, "User not found", null, null, AuthErrorCode.USER_NOT_FOUND);
        }
    }


}
