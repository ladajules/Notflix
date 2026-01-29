package com.ladajules.notflix.utils

import com.ladajules.notflix.utils.Constants

object ValidationUtils {
    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String? = null
    )

    fun validateName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult(false, "Name cannot be empty")
            name.length > Constants.MAX_NAME_LENGTH -> ValidationResult(
                false,
                "Name must be less than ${Constants.MAX_NAME_LENGTH} characters"
            )
            !name.matches(Regex("^[a-zA-Z ]+$")) -> ValidationResult(
                false,
                "Name can only contain letters and spaces"
            )
            else -> ValidationResult(true)
        }
    }

    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult(false, "Email cannot be empty")
            !email.isValidEmail() -> ValidationResult(false, "Please enter a valid email address")
            else -> ValidationResult(true)
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult(false, "Password cannot be empty")
            password.length < Constants.MIN_PASSWORD_LENGTH -> ValidationResult(
                false,
                "Password must be at least ${Constants.MIN_PASSWORD_LENGTH} characters"
            )
            else -> ValidationResult(true)
        }
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isBlank() -> ValidationResult(false, "Please confirm your password")
            password != confirmPassword -> ValidationResult(false, "Passwords do not match")
            else -> ValidationResult(true)
        }
    }

    fun getPasswordStrength(password: String): PasswordStrength {
        val strength = password.isValidPassword()
        return when (strength) {
            0 -> PasswordStrength.WEAK
            1 -> PasswordStrength.MEDIUM
            else -> PasswordStrength.STRONG
        }
    }

    enum class PasswordStrength(val label: String, val color: Int) {
        WEAK("Weak", android.R.color.holo_red_dark),
        MEDIUM("Medium", android.R.color.holo_orange_dark),
        STRONG("Strong", android.R.color.holo_green_dark)
    }
}