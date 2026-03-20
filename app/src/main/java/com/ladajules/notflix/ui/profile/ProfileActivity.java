package com.ladajules.notflix.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ladajules.notflix.R;
import com.ladajules.notflix.data.repository.ProfileRepository;
import com.ladajules.notflix.databinding.ActivityProfileBinding;
import com.ladajules.notflix.ui.download.DownloadsActivity;
import com.ladajules.notflix.ui.main.MainActivity;
import com.ladajules.notflix.ui.search.SearchActivity;
import com.ladajules.notflix.utils.PreferenceManager;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private PreferenceManager preferenceManager;
    private ProfileRepository profileRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        profileRepository = new ProfileRepository();

        setupListeners();
        loadUserProfile();
    }

    private void setupListeners() {
        binding.ivProfileSearch.setOnClickListener(v -> 
                startActivity(new Intent(ProfileActivity.this, SearchActivity.class)));

        binding.ivProfileMenu.setOnClickListener(v -> {
            // TODO: Open settings or menu
        });

        binding.profileContainer.setOnClickListener(v -> {
            // Switch profile
            startActivity(new Intent(ProfileActivity.this, ProfileSelectionActivity.class));
        });

        binding.btnSeeDownloads.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        binding.bottomNavBar.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                return false;
            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(ProfileActivity.this, SearchActivity.class));
                return false;
            } else if (itemId == R.id.nav_profile) {
                return true;
            } else if (itemId == R.id.nav_downloads) {
                startActivity(new Intent(ProfileActivity.this, DownloadsActivity.class));
                return false;
            }
            return true;
        });
    }

    private void loadUserProfile() {
        String profileId = preferenceManager.getSelectedProfileId();
        if (profileId != null) {
            profileRepository.getProfile(profileId, (success, profile, e) -> {
                if (success && profile != null) {
                    binding.tvProfileName.setText(profile.getName());
                    setProfileAvatar(binding.ivProfileAvatar, profile.getAvatarUrl());
                }
            });
        }
    }

    private void setProfileAvatar(ImageView imageView, String avatarUrl) {
        int avatarRes;
        if (avatarUrl == null) {
            avatarRes = R.drawable.avatar_default;
        } else {
            switch (avatarUrl.toLowerCase()) {
                case "pink": avatarRes = R.drawable.avatar_pink; break;
                case "green": avatarRes = R.drawable.avatar_green; break;
                case "orange": avatarRes = R.drawable.avatar_orange; break;
                case "yellow": avatarRes = R.drawable.avatar_yellow; break;
                case "blue": avatarRes = R.drawable.avatar_blue; break;
                default: avatarRes = R.drawable.avatar_default; break;
            }
        }
        imageView.setImageResource(avatarRes);
    }
}
