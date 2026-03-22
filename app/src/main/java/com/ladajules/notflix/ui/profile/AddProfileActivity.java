package com.ladajules.notflix.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ladajules.notflix.R;
import com.ladajules.notflix.data.model.Profile;
import com.ladajules.notflix.data.repository.ProfileRepository;
import com.ladajules.notflix.databinding.ActivityAddProfileBinding;
import com.ladajules.notflix.utils.PreferenceManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddProfileActivity extends AppCompatActivity {

    public static final String EXTRA_PROFILE_ID = "extra_profile_id";
    public static final String EXTRA_PROFILE_NAME = "extra_profile_name";
    public static final String EXTRA_PROFILE_AVATAR = "extra_profile_avatar";

    private ActivityAddProfileBinding binding;
    private PreferenceManager preferenceManager;
    private ProfileRepository profileRepository;
    private String selectedAvatar = "default";
    private String editProfileId = null;

    private final List<String> availableAvatars = Arrays.asList(
            "pink", "green", "orange", "yellow", "blue", "default"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        profileRepository = new ProfileRepository();

        checkEditMode();
        setupListeners();
        setupAvatarRecyclerView();
    }

    private void checkEditMode() {
        editProfileId = getIntent().getStringExtra(EXTRA_PROFILE_ID);
        if (editProfileId != null) {
            String name = getIntent().getStringExtra(EXTRA_PROFILE_NAME);
            selectedAvatar = getIntent().getStringExtra(EXTRA_PROFILE_AVATAR);
            
            binding.etProfileName.setText(name);
            setProfileAvatar(binding.ivAvatar, selectedAvatar);
            // Optional: Change title to "Edit Profile"
        }
    }

    private void setupListeners() {
        binding.btnCancel.setOnClickListener(v -> finish());

        binding.btnSave.setOnClickListener(v -> saveProfile());

        binding.ivEditAvatar.setOnClickListener(v -> {
            if (binding.rvAvatars.getVisibility() == View.VISIBLE) {
                binding.rvAvatars.setVisibility(View.GONE);
            } else {
                binding.rvAvatars.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupAvatarRecyclerView() {
        binding.rvAvatars.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvAvatars.setAdapter(new AvatarAdapter(availableAvatars, avatar -> {
            selectedAvatar = avatar;
            setProfileAvatar(binding.ivAvatar, avatar);
            binding.rvAvatars.setVisibility(View.GONE);
        }));
    }

    private void saveProfile() {
        String name = binding.etProfileName.getText().toString().trim();
        String userId = preferenceManager.getUserId();

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userId == null) {
            Toast.makeText(this, "User error. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (editProfileId != null) {
            // Update existing profile
            Map<String, Object> updates = new HashMap<>();
            updates.put("name", name);
            updates.put("avatarUrl", selectedAvatar);

            profileRepository.updateProfile(editProfileId, updates, (success, data, e) -> {
                if (success) {
                    Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Create new profile
            Profile profile = new Profile(
                    UUID.randomUUID().toString(),
                    userId,
                    name,
                    selectedAvatar,
                    System.currentTimeMillis()
            );

            profileRepository.createProfile(profile, (success, id, e) -> {
                if (success) {
                    Toast.makeText(this, "Profile created!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error: " + (e != null ? e.getMessage() : "Unknown"), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setProfileAvatar(ImageView imageView, String avatarUrl) {
        int avatarRes;
        if (avatarUrl == null) avatarUrl = "default";
        switch (avatarUrl.toLowerCase()) {
            case "pink": avatarRes = R.drawable.avatar_pink; break;
            case "green": avatarRes = R.drawable.avatar_green; break;
            case "orange": avatarRes = R.drawable.avatar_orange; break;
            case "yellow": avatarRes = R.drawable.avatar_yellow; break;
            case "blue": avatarRes = R.drawable.avatar_blue; break;
            default: avatarRes = R.drawable.avatar_default; break;
        }
        imageView.setImageResource(avatarRes);
    }

    private class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.ViewHolder> {
        private final List<String> avatars;
        private final OnAvatarClickListener listener;

        public AvatarAdapter(List<String> avatars, OnAvatarClickListener listener) {
            this.avatars = avatars;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_avatar_choice, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String avatar = avatars.get(position);
            setProfileAvatar(holder.ivAvatar, avatar);
            holder.itemView.setOnClickListener(v -> listener.onAvatarClick(avatar));
        }

        @Override
        public int getItemCount() {
            return avatars.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivAvatar;
            ViewHolder(View itemView) {
                super(itemView);
                ivAvatar = itemView.findViewById(R.id.ivAvatarChoice);
            }
        }
    }

    interface OnAvatarClickListener {
        void onAvatarClick(String avatar);
    }
}
