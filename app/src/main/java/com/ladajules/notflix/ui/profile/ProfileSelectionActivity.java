package com.ladajules.notflix.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ladajules.notflix.adapter.ProfileAdapter;
import com.ladajules.notflix.data.model.Profile;
import com.ladajules.notflix.data.repository.ProfileRepository;
import com.ladajules.notflix.databinding.ActivityProfileSelectionBinding;
import com.ladajules.notflix.ui.main.MainActivity;
import com.ladajules.notflix.utils.Constants;
import com.ladajules.notflix.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class ProfileSelectionActivity extends AppCompatActivity {

    private ActivityProfileSelectionBinding binding;
    private ProfileAdapter profileAdapter;
    private ProfileRepository profileRepository;
    private PreferenceManager preferenceManager;
    
    private static final String TAG = "ProfileSelectionActivity";
    private List<Profile> profiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        profileRepository = new ProfileRepository();

        setupRecyclerView();
        setupListeners();
        loadProfiles();
    }

    private void setupRecyclerView() {
        profileAdapter = new ProfileAdapter(
            this::onProfileSelected,
            this::showAddProfileDialog
        );

        // Use GridLayoutManager with 2 columns
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.rvProfiles.setLayoutManager(layoutManager);
        binding.rvProfiles.setAdapter(profileAdapter);
    }

    private void setupListeners() {
        binding.tvEdit.setOnClickListener(v -> {
            // TODO: Implement edit mode for profiles
        });
    }

    private void loadProfiles() {
        String userId = preferenceManager.getUserId();
        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        profileRepository.getProfilesForUser(userId, (success, profileList, e) -> {
            if (success && profileList != null) {
                profiles = profileList;
                profileAdapter.submitList(profiles, Constants.MAX_PROFILES_PER_USER);
                
                // if user has no profiles, show message to add one
                if (profiles.isEmpty()) {
                    Toast.makeText(
                        ProfileSelectionActivity.this, 
                        "Please add a profile to continue", 
                        Toast.LENGTH_LONG
                    ).show();
                }
            } else {
                Log.e(TAG, "Failed to load profiles", e);
                String errorMessage = (e != null) ? e.getMessage() : "Unknown error";
                Toast.makeText(
                    ProfileSelectionActivity.this, 
                    "Failed to load profiles: " + errorMessage, 
                    Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void onProfileSelected(Profile profile) {
        // Save selected profile
        preferenceManager.setSelectedProfileId(profile.getId());
        
        Log.d(TAG, "Profile selected: " + profile.getName());
        
        // Navigate to MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showAddProfileDialog() {
        String userId = preferenceManager.getUserId();
        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        AddProfileDialog dialog = new AddProfileDialog(userId, this::createProfile);
        dialog.show(getSupportFragmentManager(), "AddProfileDialog");
    }

    private void createProfile(Profile profile) {
        profileRepository.createProfile(profile, (success, profileId, e) -> {
            if (success) {
                Log.d(TAG, "Profile created with ID: " + profileId);
                Toast.makeText(
                    ProfileSelectionActivity.this, 
                    "Profile created successfully", 
                    Toast.LENGTH_SHORT
                ).show();

                loadProfiles();
            } else {
                Log.e(TAG, "Failed to create profile", e);
                String errorMessage = (e != null) ? e.getMessage() : "Unknown error";
                Toast.makeText(
                    ProfileSelectionActivity.this, 
                    "Failed to create profile: " + errorMessage, 
                    Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfiles();
    }
}
