package com.ladajules.notflix.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.ladajules.notflix.adapter.ProfileAdapter
import com.ladajules.notflix.data.model.Profile
import com.ladajules.notflix.data.repository.ProfileRepository
import com.ladajules.notflix.databinding.ActivityProfileSelectionBinding
import com.ladajules.notflix.ui.main.MainActivity
import com.ladajules.notflix.utils.PreferenceManager
import com.ladajules.notflix.utils.Constants
import kotlinx.coroutines.launch
import android.util.Log

class ProfileSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileSelectionBinding
    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var profileRepository: ProfileRepository
    private lateinit var preferenceManager: PreferenceManager
    
    private val TAG = "ProfileSelectionActivity"
    private var profiles: List<Profile> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)
        profileRepository = ProfileRepository()

        setupRecyclerView()
        setupListeners()
        loadProfiles()
    }

    private fun setupRecyclerView() {
        profileAdapter = ProfileAdapter(
            onProfileClick = { profile ->
                onProfileSelected(profile)
            },
            onAddProfileClick = {
                showAddProfileDialog()
            }
        )

        // Use GridLayoutManager with 2 columns
        val layoutManager = GridLayoutManager(this, 2)
        binding.rvProfiles.layoutManager = layoutManager
        binding.rvProfiles.adapter = profileAdapter
    }

    private fun setupListeners() {
        binding.tvEdit.setOnClickListener {
            // TODO: Implement edit mode for profiles
        }
    }

    private fun loadProfiles() {
        val userId = preferenceManager.userId
        if (userId == null) {
            //Log.e(TAG, "No user ID found")
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val result = profileRepository.getProfilesForUser(userId)
            
            result.onSuccess { profileList ->
                profiles = profileList
                profileAdapter.submitList(profiles, Constants.MAX_PROFILES_PER_USER)
                
                // if user has no profiles, show message to add one
                if (profiles.isEmpty()) {
                    Toast.makeText(
                        this@ProfileSelectionActivity, 
                        "Please add a profile to continue", 
                        Toast.LENGTH_LONG
                    ).show()
                }
            }.onFailure { error ->
                Log.e(TAG, "Failed to load profiles", error)
                Toast.makeText(
                    this@ProfileSelectionActivity, 
                    "Failed to load profiles: ${error.message}", 
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun onProfileSelected(profile: Profile) {
        // Save selected profile
        preferenceManager.selectedProfileId = profile.id
        
        Log.d(TAG, "Profile selected: ${profile.name}")
        
        // Navigate to MainActivity
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showAddProfileDialog() {
        val userId = preferenceManager.userId
        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val dialog = AddProfileDialog(userId) { newProfile ->
            createProfile(newProfile)
        }
        dialog.show(supportFragmentManager, "AddProfileDialog")
    }

    private fun createProfile(profile: Profile) {
        lifecycleScope.launch {
            val result = profileRepository.createProfile(profile)
            
            result.onSuccess { profileId ->
                Log.d(TAG, "Profile created with ID: $profileId")
                Toast.makeText(
                    this@ProfileSelectionActivity, 
                    "Profile created successfully", 
                    Toast.LENGTH_SHORT
                ).show()

                loadProfiles()
            }.onFailure { error ->
                Log.e(TAG, "Failed to create profile", error)
                Toast.makeText(
                    this@ProfileSelectionActivity, 
                    "Failed to create profile: ${error.message}", 
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadProfiles()
    }
}