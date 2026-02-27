package com.ladajules.notflix.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ladajules.notflix.R
import com.ladajules.notflix.data.repository.AuthRepository
import com.ladajules.notflix.data.repository.UserRepository
import com.ladajules.notflix.databinding.ActivityLoginBinding
import com.ladajules.notflix.ui.onboarding.OnboardingActivity
import com.ladajules.notflix.ui.profile.ProfileSelectionActivity
import com.ladajules.notflix.utils.PreferenceManager
import com.ladajules.notflix.utils.ValidationUtils
import com.ladajules.notflix.utils.hideKeyboard
import com.ladajules.notflix.utils.showToast
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize repositories
        authRepository = AuthRepository()
        userRepository = UserRepository()
        preferenceManager = PreferenceManager(this)

        setupUI()
        setupListeners()
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
        // Sign In Button
        binding.btnSignIn.setOnClickListener {
            hideKeyboard()
            validateAndSignIn()
        }

        // Sign Up Link
        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        // Back Button
        binding.ibBack.setOnClickListener {
            finish()
        }
    }

    private fun validateAndSignIn() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val rememberMe = binding.checkboxRememberMe.isChecked

        // Clear previous errors
        binding.tilEmail.error = null
        binding.tilPassword.error = null

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

        // Proceed with sign in
        signIn(email, password, rememberMe)
    }

    private fun signIn(email: String, password: String, rememberMe: Boolean) {
        showLoading(true)

        lifecycleScope.launch {
            when (val result = authRepository.signIn(email, password)) {
                is AuthRepository.AuthResult.Success -> {
                    // Update last login
                    userRepository.updateLastLogin(result.userId)

                    // Get user data to check onboarding status
                    val userResult = userRepository.getUser(result.userId)

                    if (userResult.isSuccess) {
                        val user = userResult.getOrNull()

                        if (user != null) {
                            // Save user session
                            preferenceManager.isLoggedIn = true
                            preferenceManager.userId = result.userId
                            preferenceManager.rememberMe = rememberMe

                            showLoading(false)

                            // Check if user has completed onboarding
                            if (user.hasCompletedOnboarding) {
                                // Go directly to Profile Selection
                                navigateToProfileSelection()
                            } else {
                                // First time login - show onboarding
                                navigateToOnboarding()
                            }
                        } else {
                            showLoading(false)
                            showToast("User data not found. Please try again.")
                        }
                    } else {
                        showLoading(false)
                        showToast("Failed to load user data. Please try again.")
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

    private fun navigateToProfileSelection() {
        val intent = Intent(this, ProfileSelectionActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        //binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnSignIn.isEnabled = !isLoading
        binding.etEmail.isEnabled = !isLoading
        binding.etPassword.isEnabled = !isLoading
    }
}
