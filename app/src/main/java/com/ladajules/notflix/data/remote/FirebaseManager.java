package com.ladajules.notflix.data.remote;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseManager {

    private static FirebaseAuth auth;
    private static FirebaseFirestore firestore;

    public static synchronized FirebaseAuth getAuth() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public static synchronized FirebaseFirestore getFirestore() {
        if (firestore == null) {
            firestore = FirebaseFirestore.getInstance();
        }
        return firestore;
    }

    public static String getCurrentUserId() {
        return getAuth().getCurrentUser() != null ? getAuth().getCurrentUser().getUid() : null;
    }

    public static boolean isUserLoggedIn() {
        return getAuth().getCurrentUser() != null;
    }
}
