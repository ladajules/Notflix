package com.ladajules.notflix.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ladajules.notflix.data.model.User;
import com.ladajules.notflix.data.remote.FirebaseManager;
import com.ladajules.notflix.utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private final FirebaseFirestore firestore;

    public UserRepository() {
        this.firestore = FirebaseManager.getFirestore();
    }

    public interface UserCallback<T> {
        void onResult(boolean success, T data, Exception e);
    }

    public void createUser(User user, UserCallback<Void> callback) {
        firestore.collection(Constants.USERS_COLLECTION)
                .document(user.getId())
                .set(user.toMap())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onResult(true, null, null);
                    } else {
                        callback.onResult(false, null, task.getException());
                    }
                });
    }

    public void getUser(String userId, UserCallback<User> callback) {
        firestore.collection(Constants.USERS_COLLECTION)
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            User user = User.fromMap(document.getData());
                            callback.onResult(true, user, null);
                        } else {
                            callback.onResult(true, null, null);
                        }
                    } else {
                        callback.onResult(false, null, task.getException());
                    }
                });
    }

    public void updateUser(String userId, Map<String, Object> updates, UserCallback<Void> callback) {
        firestore.collection(Constants.USERS_COLLECTION)
                .document(userId)
                .update(updates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onResult(true, null, null);
                    } else {
                        callback.onResult(false, null, task.getException());
                    }
                });
    }

    public void updateLastLogin(String userId, UserCallback<Void> callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("lastLoginAt", System.currentTimeMillis());
        
        updateUser(userId, updates, callback);
    }
}
