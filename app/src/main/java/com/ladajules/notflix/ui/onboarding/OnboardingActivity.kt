package com.ladajules.notflix.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.ladajules.notflix.R
import com.ladajules.notflix.adapter.OnboardingAdapter
import com.ladajules.notflix.data.model.OnboardingItem
import com.ladajules.notflix.databinding.ActivityOnboardingBinding
import com.ladajules.notflix.ui.landing.LandingActivity
import com.ladajules.notflix.utils.PreferenceManager

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var onboardingAdapter: OnboardingAdapter

    private val onboardingItems = listOf(
        OnboardingItem(
            title = "Unlimited entertainment, one low price",
            description = "All of Notflix, starting at just ₱149. No extra costs, no contracts.",
            imageResId = R.drawable.onboarding_1
        ),
        OnboardingItem(
            title = "Download and go",
            description = "Save your favorites easily and always have something to watch.",
            imageResId = R.drawable.onboarding_2
        ),
        OnboardingItem(
            title = "No ads, no interruptions",
            description = "Watch anywhere, anytime on an unlimited number of devices.",
            imageResId = R.drawable.onboarding_3
        ),
        OnboardingItem(
            title = "Every kind of movie and TV show",
            description = "Watch on Smart TVs, PlayStation, Xbox, Chromecast, Apple TV and more.",
            imageResId = R.drawable.onboarding_4
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)

        setupUI()
        setupViewPager()
        setupListeners()
    }

    private fun setupUI() {
        // Make status bar transparent
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
    }

    private fun setupViewPager() {
        onboardingAdapter = OnboardingAdapter(onboardingItems)
        binding.viewPager.adapter = onboardingAdapter

        // Setup dots indicator
        TabLayoutMediator(binding.tabLayoutOnboarding, binding.viewPager) { _, _ -> }.attach()

        // Listen to page changes
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateButtonText(position)
            }
        })
    }

    private fun setupListeners() {
        binding.btnNext.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem < onboardingItems.size - 1) {
                binding.viewPager.currentItem = currentItem + 1
            } else {
                finishOnboarding()
            }
        }
    }

    private fun updateButtonText(position: Int) {
        if (position == onboardingItems.size - 1) {
            // Last page
            binding.btnNext.text = "Get Started"
        } else {
            // Other pages
            binding.btnNext.text = "Next"
        }
    }

    private fun finishOnboarding() {
        preferenceManager.onboardingCompleted = true // temporary

        val intent = Intent(this, LandingActivity::class.java)
        //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        //finish()

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
