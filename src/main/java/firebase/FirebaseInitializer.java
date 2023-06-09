package firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FirebaseInitializer {

    @PostConstruct
    private  void initFirestore() throws IOException {

        InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("private-key-firebase.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://inventoryhub-8a0b1.firebaseio.com/")
                .build();

        if(FirebaseApp.getApps().isEmpty()){
            FirebaseApp.initializeApp(options);
        }
    }

    public Firestore getFirestore(){
        return FirestoreClient.getFirestore();
    }
    public FirebaseAuth getFirebaseAuth() {
        return getFirebaseAuth();
    }
}
