package com.ladajules.notflix.data.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.ladajules.notflix.data.remote.FirebaseManager;

public class AuthRepository {

    private final FirebaseAuth auth;

    public AuthRepository() {
        this.auth = FirebaseManager.getAuth();
    }

    public interface AuthCallback {
        void onResult(AuthResult result);
    }

    public static abstract class AuthResult {
        private AuthResult() {}
        
        public static class Success extends AuthResult {
            private final String userId;
            public Success(String userId) {
                this.userId = userId;
            }
            public String getUserId() {
                return userId;
            }
        }

        public static class Error extends AuthResult {
            private final String message;
            public Error(String message) {
                this.message = message;
            }
            public String getMessage() {
                return message;
            }
        }
    }

    public void signUp(String email, String password, AuthCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().getUser() != null) {
                    callback.onResult(new AuthResult.Success(task.getResult().getUser().getUid()));
                } else {
                    Exception e = task.getException();
                    String message;
                    if (e instanceof IllegalStateException) {
                        message = "Firebase not initialized. Check google-services.json.";
                    } else if (e instanceof FirebaseAuthWeakPasswordException) {
                        message = "Password is too weak. Please use a stronger password.";
                    } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        message = "Invalid email format.";
                    } else if (e instanceof FirebaseAuthUserCollisionException) {
                        message = "An account with this email already exists.";
                    } else {
                        message = (e != null && e.getMessage() != null) ? e.getMessage() : "An unknown error occurred.";
                    }
                    callback.onResult(new AuthResult.Error(message));
                }
            });
    }

    public void signIn(String email, String password, AuthCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().getUser() != null) {
                    callback.onResult(new AuthResult.Success(task.getResult().getUser().getUid()));
                } else {
                    Exception e = task.getException();
                    String message;
                    if (e instanceof IllegalStateException) {
                        message = "Firebase not initialized. Check google-services.json.";
                    } else if (e instanceof FirebaseAuthInvalidUserException) {
                        message = "No account found with this email.";
                    } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        message = "Incorrect password. Please try again.";
                    } else {
                        message = (e != null && e.getMessage() != null) ? e.getMessage() : "An unknown error occurred.";
                    }
                    callback.onResult(new AuthResult.Error(message));
                }
            });
    }

    public void signOut() {
        auth.signOut();
    }

    public String getCurrentUserId() {
        FirebaseUser user = auth.getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    public boolean isUserLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }
}
