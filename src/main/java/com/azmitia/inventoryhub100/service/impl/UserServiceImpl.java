package com.azmitia.inventoryhub100.service.impl;

import com.azmitia.inventoryhub100.dto.UserDTO;
import com.azmitia.inventoryhub100.service.UserService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import firebase.FirebaseInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private FirebaseInitializer firebase;

    @Override
    public UserDTO createUser(UserDTO user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", user.getEmail());
        userMap.put("password", user.getPassword());

        CollectionReference users = firebase.getFirestore().collection("user");
        ApiFuture<DocumentReference> documentReferenceApiFuture = users.add(userMap);

        try {
            if (null != documentReferenceApiFuture.get()) {
                user.setId(documentReferenceApiFuture.get().getId());
                return user;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public UserDTO updateUser(String id, String newPassword) {
        DocumentReference userRef = getCollection().document(id);
        try {
            userRef.update("password", newPassword);
            System.out.println("User password updated successfully");
            return getUserById(id);
        } catch (Exception e) {
            System.out.println("Error updating user password: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteUser(String id) {
        ApiFuture<WriteResult> writeResult = getCollection().document(id).delete();
        try {
            writeResult.get();
            System.out.println("User deleted successfully");
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }

        DocumentSnapshot documentSnapshot = null;
        try {
            documentSnapshot = getCollection().document(id).get().get();

        } catch (Exception e) {
            System.out.println("Error not found: " + e.getMessage());
        }
        if (documentSnapshot.exists()) {
            System.out.println("Error deleting user: user still exists in the database");
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = new ArrayList<>();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();

        try {
            for (DocumentSnapshot documentSnapshot : querySnapshotApiFuture.get().getDocuments()) {
                UserDTO user = documentSnapshot.toObject(UserDTO.class);
                user.setId(documentSnapshot.getId());
                users.add(user);
            }
            return users;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public UserDTO getUserById(String id) {
        DocumentReference docRef = getCollection().document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();

        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                UserDTO user = document.toObject(UserDTO.class);
                user.setId(id);
                return user;
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("user");
    }
}
