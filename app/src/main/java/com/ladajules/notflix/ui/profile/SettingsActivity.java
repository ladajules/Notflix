package com.ladajules.notflix.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.ladajules.notflix.data.repository.DownloadRepository;
import com.ladajules.notflix.databinding.ActivitySettingsBinding;
import com.ladajules.notflix.ui.landing.LandingActivity;
import com.ladajules.notflix.utils.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private PreferenceManager preferenceManager;
    private DownloadRepository downloadRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        downloadRepository = new DownloadRepository();

        setupListeners();
    }

    private void setupListeners() {
        binding.ivBack.setOnClickListener(v -> finish());

        binding.llDeleteAll.setOnClickListener(v -> showDeleteConfirmation());

        binding.llSpeedTest.setOnClickListener(v -> openUrl("https://fast.com"));

        binding.llPrivacyPolicy.setOnClickListener(v -> openUrl("https://help.netflix.com/legal/privacy"));

        binding.llHelp.setOnClickListener(v -> openUrl("https://help.netflix.com"));

        binding.btnSignOut.setOnClickListener(v -> signOut());
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Delete All Downloads")
                .setMessage("Are you sure you want to delete all your downloaded movies?")
                .setPositiveButton("Delete", (dialog, which) -> deleteAllDownloads())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteAllDownloads() {
        String profileId = preferenceManager.getSelectedProfileId();
        if (profileId != null) {
            downloadRepository.deleteAllDownloads(profileId, (success, data, e) -> {
                if (success) {
                    Toast.makeText(this, "All downloads deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to delete downloads", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        preferenceManager.clearSession();
        
        Intent intent = new Intent(this, LandingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
