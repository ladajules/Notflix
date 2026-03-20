package com.ladajules.notflix.ui.auth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ladajules.notflix.data.model.User;
import com.ladajules.notflix.databinding.ActivityLoginBinding;
import com.ladajules.notflix.ui.onboarding.OnboardingActivity;
import com.ladajules.notflix.ui.profile.ProfileSelectionActivity;
import com.ladajules.notflix.utils.Constants;
import com.ladajules.notflix.utils.Extensions;
import com.ladajules.notflix.utils.PreferenceManager;
import com.ladajules.notflix.utils.ValidationUtils;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(this);

        setupUI();
        setupListeners();
    }

    private void setupUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    private void setupListeners() {
        binding.btnSignIn.setOnClickListener(v -> {
            Extensions.hideKeyboard(this);
            validateAndSignIn();
        });

        binding.tvSignUp.setOnClickListener(v -> 
            startActivity(new Intent(this, SignupActivity.class))
        );

        binding.ibBack.setOnClickListener(v -> finish());
    }

    private void validateAndSignIn() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        boolean rememberMe = binding.checkboxRememberMe.isChecked();

        binding.tilEmail.setError(null);
        binding.tilPassword.setError(null);

        ValidationUtils.ValidationResult emailValidation = ValidationUtils.validateEmail(email);
        if (!emailValidation.isValid()) {
            binding.tilEmail.setError(emailValidation.getErrorMessage());
            return;
        }

        ValidationUtils.ValidationResult passwordValidation = ValidationUtils.validatePassword(password);
        if (!passwordValidation.isValid()) {
            binding.tilPassword.setError(passwordValidation.getErrorMessage());
            return;
        }

        signIn(email, password, rememberMe);
    }

    private void signIn(String email, String password, boolean rememberMe) {
        showLoading(true);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();
                        updateLastLoginAndFetchUser(userId, rememberMe);
                    } else {
                        showLoading(false);
                        Extensions.showToast(this, "Authentication failed: " + task.getException().getMessage());
                    }
                });
    }

    private void updateLastLoginAndFetchUser(String userId, boolean rememberMe) {
        mFirestore.collection(Constants.USERS_COLLECTION)
                .document(userId)
                .update("lastLoginAt", System.currentTimeMillis())
                .addOnCompleteListener(task -> {
                    fetchUserData(userId, rememberMe);
                });
    }

    private void fetchUserData(String userId, boolean rememberMe) {
        mFirestore.collection(Constants.USERS_COLLECTION)
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    showLoading(false);
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> data = document.getData();
                            User user = User.fromMap(data);

                            if (user != null) {
                                preferenceManager.setLoggedIn(true);
                                preferenceManager.setUserId(userId);
                                preferenceManager.setRememberMe(rememberMe);

                                if (user.isHasCompletedOnboarding()) {
                                    navigateToProfileSelection();
                                } else {
                                    navigateToOnboarding();
                                }
                            } else {
                                Extensions.showToast(this, "User data not found.");
                            }
                        } else {
                            Extensions.showToast(this, "User document does not exist.");
                        }
                    } else {
                        Extensions.showToast(this, "Failed to load user data.");
                    }
                });
    }

    private void navigateToOnboarding() {
        Intent intent = new Intent(this, OnboardingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void navigateToProfileSelection() {
        Intent intent = new Intent(this, ProfileSelectionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.btnSignIn.setEnabled(!isLoading);
        binding.etEmail.setEnabled(!isLoading);
        binding.etPassword.setEnabled(!isLoading);
    }
}
