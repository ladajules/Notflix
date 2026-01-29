package com.ladajules.notflix.ui.landing

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.ladajules.notflix.R
import com.ladajules.notflix.adapter.HeroImageAdapter
import com.ladajules.notflix.data.model.HeroImage
import com.ladajules.notflix.databinding.ActivityLandingBinding
import com.ladajules.notflix.ui.auth.LoginActivity
import com.ladajules.notflix.ui.auth.SignupActivity

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding
    private lateinit var heroImageAdapter: HeroImageAdapter

    // Auto-scroll handler
    private val sliderHandler = Handler(Looper.getMainLooper())
    private val sliderRunnable = Runnable {
        if (binding.viewPagerHero.currentItem < heroImages.size - 1) {
            binding.viewPagerHero.currentItem = binding.viewPagerHero.currentItem + 1
        } else {
            binding.viewPagerHero.currentItem = 0
        }
    }

    // Data for the slider
    private val heroImages = listOf(
        HeroImage(R.drawable.onboarding_1, "Movies, shows,\nand games in just\na few taps"),
        HeroImage(R.drawable.onboarding_2, "Download and watch\noffline"),
        HeroImage(R.drawable.onboarding_3, "No ads,\nno interruptions"),
        HeroImage(R.drawable.onboarding_4, "Watch anywhere.\nCancel anytime.")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupViewPager()
        setupListeners()
    }

    private fun setupUI() {
        // Transparent Status Bar (Edge-to-Edge)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
    }

    private fun setupViewPager() {
        heroImageAdapter = HeroImageAdapter(heroImages)
        binding.viewPagerHero.adapter = heroImageAdapter

        // Attach Dots (TabLayout)
        TabLayoutMediator(binding.tabLayoutDots, binding.viewPagerHero) { _, _ ->
            // No text for tabs, just dots
        }.attach()

        // Auto-scroll logic
        binding.viewPagerHero.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 4000) // 4 seconds delay
            }
        })
    }

    private fun setupListeners() {
        // "Sign In" button (Top Right)
        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // "Create Account" red box (Bottom)
        // Even though text says "Go to netflix.com", we route to Signup for the app flow
        binding.footerContainer.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 4000)
    }
}