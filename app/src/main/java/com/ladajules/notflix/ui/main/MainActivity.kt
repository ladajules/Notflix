package com.ladajules.notflix.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ladajules.notflix.R
import com.ladajules.notflix.ui.search.SearchActivity

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        bottomNavBar = findViewById(R.id.bottomNavBar)
    }

    private fun setupListeners() {
        findViewById<android.view.View>(R.id.searchBar).setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        bottomNavBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    true
                }
                R.id.nav_search -> {
                    // Open SearchActivity without marking it as selected
                    startActivity(Intent(this, SearchActivity::class.java))
                    // Return false to not change selection
                    false
                }
                R.id.nav_downloads -> {
                    // TODO: Implement downloads screen
                    true
                }
                R.id.nav_more -> {
                    // TODO: Implement more menu
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Reset bottom nav to Home when returning to this activity
        bottomNavBar.selectedItemId = R.id.nav_home
    }
}