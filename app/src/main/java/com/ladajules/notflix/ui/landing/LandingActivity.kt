package com.ladajules.notflix.ui.landing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.ladajules.notflix.adapter.LandingPagerAdapter
import com.ladajules.notflix.databinding.ActivityLandingBinding
import com.ladajules.notflix.ui.auth.SignInActivity
import com.ladajules.notflix.ui.auth.SignupActivity

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding
    private var autoScrollRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge()
        setupViewPager()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        startAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        stopAutoScroll()
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

        // Connect TabLayout with ViewPager
        TabLayoutMediator(binding.tabLayoutLanding, binding.viewPager) { _, _ ->
            // No text needed for dots
        }.attach()
    }

    private fun setupClickListeners() {
        // Sign In button (top right)
        binding.btnSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        // Create Account button (bottom)
        binding.btnCreateAccount.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startAutoScroll() {
        stopAutoScroll() // Clear any existing callbacks first
        autoScrollRunnable = object : Runnable {
            override fun run() {
                val currentItem = binding.viewPager.currentItem
                val nextItem = (currentItem + 1) % 4
                binding.viewPager.setCurrentItem(nextItem, true)
                autoScrollRunnable?.let {
                    binding.viewPager.postDelayed(it, 3000)
                }
            }
        }
        autoScrollRunnable?.let {
            binding.viewPager.postDelayed(it, 3000)
        }
    }

    private fun stopAutoScroll() {
        autoScrollRunnable?.let {
            binding.viewPager.removeCallbacks(it)
        }
        autoScrollRunnable = null
    }
}