package com.ladajules.notflix.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ladajules.notflix.R
import com.ladajules.notflix.data.model.User
import com.ladajules.notflix.data.repository.AuthRepository
import com.ladajules.notflix.data.repository.UserRepository
import com.ladajules.notflix.databinding.ActivitySignupBinding
import com.ladajules.notflix.ui.onboarding.OnboardingActivity
import com.ladajules.notflix.ui.profile.ProfileSelectionActivity
import com.ladajules.notflix.utils.PreferenceManager
import com.ladajules.notflix.utils.ValidationUtils
import com.ladajules.notflix.utils.getColorCompat
import com.ladajules.notflix.utils.hideKeyboard
import com.ladajules.notflix.utils.showToast
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize repositories
        authRepository = AuthRepository()
        userRepository = UserRepository()
        preferenceManager = PreferenceManager(this)

        setupUI()
        setupListeners()
        setupPasswordStrengthIndicator()
    }

    private fun setupUI() {
        // Make status bar transparent
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
        window.statusBarColor = android.graphics.Color.TRANSPARENT
    }

    private fun setupListeners() {
        // Sign Up Button
        binding.btnSignUp.setOnClickListener {
            hideKeyboard()
            validateAndSignUp()
        }

        // Sign In Link
        binding.tvSignIn.setOnClickListener {
            finish() // Go back to Login
        }

        // Back Button
        binding.ibBack.setOnClickListener {
            finish()
        }
    }

    private fun setupPasswordStrengthIndicator() {
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (password.isEmpty()) {
                    binding.passwordStrengthIndicator.visibility = View.GONE
                    return
                }

                val strength = ValidationUtils.getPasswordStrength(password)
                binding.passwordStrengthIndicator.visibility = View.VISIBLE
                binding.tvPasswordStrength.text = "Password Strength: ${strength.label}"

                // Update indicator color and progress
                val color = getColorCompat(strength.color)
                binding.tvPasswordStrength.setTextColor(color)
                binding.progressPasswordStrength.progressTintList =
                    android.content.res.ColorStateList.valueOf(color)

                when (strength) {
                    ValidationUtils.PasswordStrength.WEAK -> binding.progressPasswordStrength.progress = 33
                    ValidationUtils.PasswordStrength.MEDIUM -> binding.progressPasswordStrength.progress = 66
                    ValidationUtils.PasswordStrength.STRONG -> binding.progressPasswordStrength.progress = 100
                }
            }
        })
    }

    private fun validateAndSignUp() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // Clear previous errors
        binding.tilName.error = null
        binding.tilEmail.error = null
        binding.tilPassword.error = null

        // Validate name
        val nameValidation = ValidationUtils.validateName(name)
        if (!nameValidation.isValid) {
            binding.tilName.error = nameValidation.errorMessage
            return
        }

        // Validate email
        val emailValidation = ValidationUtils.validateEmail(email)
        if (!emailValidation.isValid) {
            binding.tilEmail.error = emailValidation.errorMessage
            return
        }

        // Validate password
        val passwordValidation = ValidationUtils.validatePassword(password)
        if (!passwordValidation.isValid) {
            binding.tilPassword.error = passwordValidation.errorMessage
            return
        }

        // Proceed with sign up
        signUp(name, email, password)
    }

    private fun signUp(name: String, email: String, password: String) {
        showLoading(true)

        lifecycleScope.launch {
            // First, create Firebase Auth account
            when (val result = authRepository.signUp(email, password)) {
                is AuthRepository.AuthResult.Success -> {
                    // Create user document in Firestore
                    val user = User(
                        id = result.userId,
                        name = name,
                        email = email,
                        hasCompletedOnboarding = false,
                        createdAt = System.currentTimeMillis(),
                        lastLoginAt = System.currentTimeMillis()
                    )

                    val userResult = userRepository.createUser(user)

                    if (userResult.isSuccess) {
                        // Save user session
                        preferenceManager.isLoggedIn = true
                        preferenceManager.userId = result.userId
                        preferenceManager.rememberMe = true // Auto remember on signup

                        showLoading(false)
                        showToast("Account created successfully!")

                        // Navigate to Profile Selection
                        navigateToOnboarding()
                    } else {
                        showLoading(false)
                        showToast("Failed to create user profile. Please try again.")
                    }
                }
                is AuthRepository.AuthResult.Error -> {
                    showLoading(false)
                    showToast(result.message)
                }
            }
        }
    }

    private fun navigateToOnboarding() {
        val intent = Intent(this, OnboardingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnSignUp.isEnabled = !isLoading
        binding.etName.isEnabled = !isLoading
        binding.etEmail.isEnabled = !isLoading
        binding.etPassword.isEnabled = !isLoading
    }
}
