package com.ladajules.notflix.ui.landing

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.ladajules.notflix.R
import com.ladajules.notflix.adapter.LandingPagerAdapter
import com.ladajules.notflix.databinding.ActivityLandingBinding
import com.ladajules.notflix.ui.auth.LoginActivity
import com.ladajules.notflix.ui.auth.SignupActivity

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge()
        setupViewPager()
        setupClickListeners()
    }

    private fun setupEdgeToEdge() {
        // Modern edge-to-edge display (replaces deprecated systemUiVisibility)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController?.apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        // Make status bar transparent
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
    }

    private fun setupViewPager() {
        val adapter = LandingPagerAdapter()
        binding.viewPager.adapter = adapter

        // Optional: Auto-scroll (uncomment if you want)
        // startAutoScroll()
    }

    private fun setupClickListeners() {
        // Sign In button (top right)
        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Create Account button (bottom)
        binding.btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    // Optional: Auto-scroll implementation
    private fun startAutoScroll() {
        binding.viewPager.postDelayed(object : Runnable {
            override fun run() {
                val currentItem = binding.viewPager.currentItem
                val nextItem = (currentItem + 1) % 4
                binding.viewPager.setCurrentItem(nextItem, true)
                binding.viewPager.postDelayed(this, 3000)
            }
        }, 3000)
    }
}