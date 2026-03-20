package com.ladajules.notflix.ui.auth;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ladajules.notflix.data.model.Profile;
import com.ladajules.notflix.data.model.User;
import com.ladajules.notflix.databinding.ActivitySignupBinding;
import com.ladajules.notflix.ui.onboarding.OnboardingActivity;
import com.ladajules.notflix.utils.Constants;
import com.ladajules.notflix.utils.Extensions;
import com.ladajules.notflix.utils.PreferenceManager;
import com.ladajules.notflix.utils.ValidationUtils;

import java.util.UUID;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(this);

        setupUI();
        setupListeners();
        setupPasswordStrengthIndicator();
    }

    private void setupUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    private void setupListeners() {
        binding.btnSignUp.setOnClickListener(v -> {
            Extensions.hideKeyboard(this);
            validateAndSignUp();
        });

        binding.tvSignIn.setOnClickListener(v -> finish());

        binding.ibBack.setOnClickListener(v -> finish());
    }

    private void setupPasswordStrengthIndicator() {
        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String password = s.toString();
                if (password.isEmpty()) {
                    binding.passwordStrengthIndicator.setVisibility(View.GONE);
                    return;
                }

                ValidationUtils.PasswordStrength strength = ValidationUtils.getPasswordStrength(password);
                binding.passwordStrengthIndicator.setVisibility(View.VISIBLE);
                binding.tvPasswordStrength.setText("Password Strength: " + strength.label);

                int color = Extensions.getColorCompat(SignupActivity.this, strength.color);
                binding.tvPasswordStrength.setTextColor(color);
                binding.progressPasswordStrength.setProgressTintList(ColorStateList.valueOf(color));

                switch (strength) {
                    case WEAK:
                        binding.progressPasswordStrength.setProgress(33);
                        break;
                    case MEDIUM:
                        binding.progressPasswordStrength.setProgress(66);
                        break;
                    case STRONG:
                        binding.progressPasswordStrength.setProgress(100);
                        break;
                }
            }
        });
    }

    private void validateAndSignUp() {
        String name = binding.etName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

        binding.tilName.setError(null);
        binding.tilEmail.setError(null);
        binding.tilPassword.setError(null);
        binding.tilConfirmPassword.setError(null);

        ValidationUtils.ValidationResult nameValidation = ValidationUtils.validateName(name);
        if (!nameValidation.isValid()) {
            binding.tilName.setError(nameValidation.getErrorMessage());
            return;
        }

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

        ValidationUtils.ValidationResult confirmPasswordValidation = ValidationUtils.validateConfirmPassword(password, confirmPassword);
        if (!confirmPasswordValidation.isValid()) {
            binding.tilConfirmPassword.setError(confirmPasswordValidation.getErrorMessage());
            return;
        }

        signUp(name, email, password);
    }

    private void signUp(String name, String email, String password) {
        showLoading(true);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();
                        createUserInFirestore(userId, name, email);
                    } else {
                        showLoading(false);
                        Extensions.showToast(this, "Registration failed: " + task.getException().getMessage());
                    }
                });
    }

    private void createUserInFirestore(String userId, String name, String email) {
        User user = new User(
                userId,
                name,
                email,
                false,
                System.currentTimeMillis(),
                System.currentTimeMillis()
        );

        mFirestore.collection(Constants.USERS_COLLECTION)
                .document(userId)
                .set(user.toMap())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        createDefaultProfile(userId, name);
                    } else {
                        showLoading(false);
                        Extensions.showToast(this, "Failed to create user document.");
                    }
                });
    }

    private void createDefaultProfile(String userId, String name) {
        String profileId = UUID.randomUUID().toString();
        Profile defaultProfile = new Profile(
                profileId,
                userId,
                name,
                "default",
                System.currentTimeMillis()
        );

        mFirestore.collection(Constants.PROFILES_COLLECTION)
                .document(profileId)
                .set(defaultProfile.toMap())
                .addOnCompleteListener(task -> {
                    showLoading(false);
                    if (task.isSuccessful()) {
                        preferenceManager.setLoggedIn(true);
                        preferenceManager.setUserId(userId);
                        preferenceManager.setRememberMe(true);
                        preferenceManager.setSelectedProfileId(profileId);

                        Extensions.showToast(this, "Account created successfully!");
                        navigateToOnboarding();
                    } else {
                        Extensions.showToast(this, "Failed to create default profile.");
                    }
                });
    }

    private void navigateToOnboarding() {
        Intent intent = new Intent(this, OnboardingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.btnSignUp.setEnabled(!isLoading);
        binding.etName.setEnabled(!isLoading);
        binding.etEmail.setEnabled(!isLoading);
        binding.etPassword.setEnabled(!isLoading);
        binding.etConfirmPassword.setEnabled(!isLoading);
    }
}
