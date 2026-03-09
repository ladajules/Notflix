package com.ladajules.notflix.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.ladajules.notflix.R
import com.ladajules.notflix.ui.onboarding.OnboardingActivity
import com.ladajules.notflix.ui.profile.ProfileSelectionActivity
import com.ladajules.notflix.utils.Constants
import com.ladajules.notflix.utils.PreferenceManager
import com.ladajules.notflix.ui.landing.LandingActivity
import com.ladajules.notflix.ui.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Initialize PreferenceManager
        preferenceManager = PreferenceManager(this)

        // Make the activity fullscreen
        setupFullscreen()

        // Navigate after delay
        navigateToNextScreen()
    }

    private fun setupFullscreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    private fun navigateToNextScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = when {
                // If user is logged in with Remember Me, go to Profile Selection
                preferenceManager.isLoggedIn && preferenceManager.rememberMe -> {
                    Intent(this, LandingActivity::class.java) // change to ProfileSelectionActivity after
                }
                // Otherwise, go to Login
                else -> {
                    Intent(this, LandingActivity::class.java)
                }
            }

            // Always navigate to Onboarding Page for now
            //val intent = Intent(this, OnboardingActivity::class.java)

            startActivity(intent)
            finish()

            // Add fade transition
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }, Constants.SPLASH_DELAY)
    }
}