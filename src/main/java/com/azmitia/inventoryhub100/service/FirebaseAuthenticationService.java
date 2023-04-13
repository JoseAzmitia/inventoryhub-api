package com.azmitia.inventoryhub100.service;

import com.azmitia.inventoryhub100.dto.UserDTO;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import firebase.FirebaseInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FirebaseAuthenticationService {
    private final FirebaseAuth firebaseAuth;

    @Autowired
    public FirebaseAuthenticationService(FirebaseInitializer firebaseInitializer) throws IOException {
        this.firebaseAuth = FirebaseAuth.getInstance(FirebaseApp.getInstance());
    }

    public String authenticateUser(UserDTO user) throws FirebaseAuthException {
        UserRecord userRecord = firebaseAuth.getUserByEmail(user.getEmail());
        String uid = userRecord.getUid();
        String idToken = firebaseAuth.createCustomToken(uid);
        // Aquí puedes guardar el idToken en la sesión del usuario o utilizarlo para autenticar futuras solicitudes a la API de Firebase.
        return idToken;
    }

    public String createUser(UserDTO user) throws FirebaseAuthException {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(user.getEmail())
                .setPassword(user.getPassword());

        UserRecord userRecord = firebaseAuth.createUser(request);

        // Aquí puedes guardar los datos del usuario en Firestore u otra base de datos.
        return userRecord.getUid();
    }
}
