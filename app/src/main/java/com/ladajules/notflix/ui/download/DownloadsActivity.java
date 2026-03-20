package com.ladajules.notflix.ui.download;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ladajules.notflix.adapter.DownloadAdapter;
import com.ladajules.notflix.data.model.Download;
import com.ladajules.notflix.data.repository.DownloadRepository;
import com.ladajules.notflix.databinding.ActivityDownloadsBinding;
import com.ladajules.notflix.ui.main.MainActivity;
import com.ladajules.notflix.utils.PreferenceManager;

import java.util.List;

public class DownloadsActivity extends AppCompatActivity implements DownloadAdapter.OnDownloadClickListener {

    private ActivityDownloadsBinding binding;
    private DownloadAdapter downloadAdapter;
    private DownloadRepository downloadRepository;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDownloadsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        downloadRepository = new DownloadRepository();
        preferenceManager = new PreferenceManager(this);

        setupViews();
        loadDownloads();
    }

    private void setupViews() {
        downloadAdapter = new DownloadAdapter(this);
        binding.rvDownloadsList.setAdapter(downloadAdapter);

        binding.ivBack.setOnClickListener(v -> finish());
        
        binding.btnFindDownloads.setOnClickListener(v -> {
            Intent intent = new Intent(DownloadsActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void loadDownloads() {
        String profileId = preferenceManager.getSelectedProfileId();
        if (profileId != null) {
            downloadRepository.getDownloadsForProfile(profileId, (success, data, e) -> {
                if (success && data != null) {
                    if (data.isEmpty()) {
                        showEmptyState(true);
                    } else {
                        showEmptyState(false);
                        downloadAdapter.setDownloads(data);
                    }
                } else {
                    showEmptyState(true);
                }
            });
        } else {
            showEmptyState(true);
        }
    }

    private void showEmptyState(boolean isEmpty) {
        binding.emptyStateContainer.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        binding.rvDownloadsList.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onDeleteClick(Download download) {
        downloadRepository.deleteDownload(download.getId(), (success, data, e) -> {
            if (success) {
                loadDownloads();
                Toast.makeText(this, "Download deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to delete download", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(Download download) {
        // TODO: Open video player
        Toast.makeText(this, "Playing: " + download.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
