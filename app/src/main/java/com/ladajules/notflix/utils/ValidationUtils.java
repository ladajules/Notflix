package com.ladajules.notflix.utils;

public class ValidationUtils {

    public static class ValidationResult {
        private final boolean isValid;
        private final String errorMessage;

        public ValidationResult(boolean isValid) {
            this(isValid, null);
        }

        public ValidationResult(boolean isValid, String errorMessage) {
            this.isValid = isValid;
            this.errorMessage = errorMessage;
        }

        public boolean isValid() {
            return isValid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    public enum PasswordStrength {
        WEAK("Weak", android.R.color.holo_red_dark),
        MEDIUM("Medium", android.R.color.holo_orange_dark),
        STRONG("Strong", android.R.color.holo_green_dark);

        public final String label;
        public final int color;

        PasswordStrength(String label, int color) {
            this.label = label;
            this.color = color;
        }
    }

    public static ValidationResult validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ValidationResult(false, "Name cannot be empty");
        }
        if (name.length() > Constants.MAX_NAME_LENGTH) {
            return new ValidationResult(false, "Name must be less than " + Constants.MAX_NAME_LENGTH + " characters");
        }
        if (!name.matches("^[a-zA-Z ]+$")) {
            return new ValidationResult(false, "Name can only contain letters and spaces");
        }
        return new ValidationResult(true);
    }

    public static ValidationResult validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return new ValidationResult(false, "Email cannot be empty");
        }
        if (!Extensions.isValidEmail(email)) {
            return new ValidationResult(false, "Please enter a valid email address");
        }
        return new ValidationResult(true);
    }

    public static ValidationResult validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return new ValidationResult(false, "Password cannot be empty");
        }
        if (password.length() < Constants.MIN_PASSWORD_LENGTH) {
            return new ValidationResult(false, "Password must be at least " + Constants.MIN_PASSWORD_LENGTH + " characters");
        }
        return new ValidationResult(true);
    }

    public static ValidationResult validateConfirmPassword(String password, String confirmPassword) {
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            return new ValidationResult(false, "Please confirm your password");
        }
        if (!confirmPassword.equals(password)) {
            return new ValidationResult(false, "Passwords do not match");
        }
        return new ValidationResult(true);
    }

    public static PasswordStrength getPasswordStrength(String password) {
        int strength = Extensions.isValidPassword(password);
        switch (strength) {
            case 0:
                return PasswordStrength.WEAK;
            case 1:
                return PasswordStrength.MEDIUM;
            default:
                return PasswordStrength.STRONG;
        }
    }

    private ValidationUtils() {}
}
