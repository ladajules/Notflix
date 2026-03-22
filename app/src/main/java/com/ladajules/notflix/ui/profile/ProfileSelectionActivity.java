package com.ladajules.notflix.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ladajules.notflix.R;
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
    private boolean isEditMode = false;

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
            this::navigateToAddProfile,
            this::onEditProfileClick
        );

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.rvProfiles.setLayoutManager(layoutManager);
        binding.rvProfiles.setAdapter(profileAdapter);
    }

    private void setupListeners() {
        binding.tvEdit.setOnClickListener(v -> {
            isEditMode = !isEditMode;
            binding.tvEdit.setText(isEditMode ? "Done" : "Edit");
            profileAdapter.setEditMode(isEditMode);
        });
    }

    private void loadProfiles() {
        String userId = preferenceManager.getUserId();
        if (userId == null) return;

        profileRepository.getProfilesForUser(userId, (success, profileList, e) -> {
            if (success && profileList != null) {
                profiles = profileList;
                profileAdapter.submitList(profiles, Constants.MAX_PROFILES_PER_USER);
            }
        });
    }

    private void onProfileSelected(Profile profile) {
        if (isEditMode) {
            onEditProfileClick(profile);
            return;
        }
        preferenceManager.setSelectedProfileId(profile.getId());
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void onEditProfileClick(Profile profile) {
        Intent intent = new Intent(this, AddProfileActivity.class);
        intent.putExtra(AddProfileActivity.EXTRA_PROFILE_ID, profile.getId());
        intent.putExtra(AddProfileActivity.EXTRA_PROFILE_NAME, profile.getName());
        intent.putExtra(AddProfileActivity.EXTRA_PROFILE_AVATAR, profile.getAvatarUrl());
        startActivity(intent);
    }

    private void navigateToAddProfile() {
        Intent intent = new Intent(this, AddProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfiles();
        // Reset edit mode when returning
        isEditMode = false;
        binding.tvEdit.setText("Edit");
        profileAdapter.setEditMode(false);
    }
}
