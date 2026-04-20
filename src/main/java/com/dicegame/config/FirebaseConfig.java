package com.dicegame.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Configura e inicializa o Firebase Admin SDK na inicialização do Spring.
 *
 * As credenciais NUNCA ficam hard-coded aqui — vêm de uma variável de
 * ambiente (FIREBASE_CREDENTIALS_PATH) que aponta para o JSON de serviço.
 * Esse arquivo JSON está no .gitignore e JAMAIS vai para o GitHub.
 */
@Configuration
public class FirebaseConfig {

    /**
     * Lê o valor de FIREBASE_CREDENTIALS_PATH do ambiente (ou do
     * application.properties que por sua vez lê do ambiente).
     * Ex: export FIREBASE_CREDENTIALS_PATH=/caminho/serviceAccountKey.json
     */
    @Value("${firebase.credentials.path}")
    private String credentialsPath;

    /**
     * Bean do Firestore — injetável em qualquer Service/Repository.
     * O método só roda uma vez; se o FirebaseApp já foi inicializado
     * (ex: em testes), reutiliza a instância existente.
     */
    @Bean
    public Firestore firestore() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            FileInputStream serviceAccount = new FileInputStream(credentialsPath);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        }

        return FirestoreClient.getFirestore();
    }
}
