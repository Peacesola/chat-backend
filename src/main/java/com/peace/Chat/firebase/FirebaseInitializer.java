package com.peace.Chat.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class FirebaseInitializer {

    @PostConstruct
    public void init() throws IOException {
        /*InputStream serviceAccount = getClass().getClassLoader()
                .getResourceAsStream("serviceAccountKey.json");*/
        String firebaseConfig = System.getenv("FIREBASE_CONFIG");
        InputStream serviceAccount = new ByteArrayInputStream(firebaseConfig.getBytes());

        FirebaseOptions options= FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        if (FirebaseApp.getApps().isEmpty()){
            FirebaseApp.initializeApp(options);
        }

    }
}
