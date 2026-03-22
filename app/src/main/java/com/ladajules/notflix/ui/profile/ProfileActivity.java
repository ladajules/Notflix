package com.ladajules.notflix.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ladajules.notflix.R;
import com.ladajules.notflix.adapter.MovieAdapter;
import com.ladajules.notflix.data.model.Download;
import com.ladajules.notflix.data.model.Movie;
import com.ladajules.notflix.data.repository.DownloadRepository;
import com.ladajules.notflix.data.repository.ProfileRepository;
import com.ladajules.notflix.data.repository.UserListRepository;
import com.ladajules.notflix.databinding.ActivityProfileBinding;
import com.ladajules.notflix.ui.details.DetailsActivity;
import com.ladajules.notflix.ui.download.DownloadsActivity;
import com.ladajules.notflix.ui.main.MainActivity;
import com.ladajules.notflix.ui.search.SearchActivity;
import com.ladajules.notflix.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private PreferenceManager preferenceManager;
    private ProfileRepository profileRepository;
    private DownloadRepository downloadRepository;
    private UserListRepository userListRepository;
    
    private MovieAdapter downloadAdapter;
    private MovieAdapter myListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        profileRepository = new ProfileRepository();
        downloadRepository = new DownloadRepository();
        userListRepository = new UserListRepository();

        setupRecyclerViews();
        setupListeners();
        loadUserProfile();
        loadDownloads();
        loadMyList();
    }

    private void setupRecyclerViews() {
        downloadAdapter = new MovieAdapter(movie -> {
            Intent intent = new Intent(ProfileActivity.this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.EXTRA_MOVIE, movie);
            startActivity(intent);
        });
        binding.rvDownloads.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvDownloads.setAdapter(downloadAdapter);

        myListAdapter = new MovieAdapter(movie -> {
            Intent intent = new Intent(ProfileActivity.this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.EXTRA_MOVIE, movie);
            startActivity(intent);
        }, true);

        binding.rvMyList.setLayoutManager(new GridLayoutManager(this, 3));
        binding.rvMyList.setNestedScrollingEnabled(false);
        binding.rvMyList.setAdapter(myListAdapter);
    }

    private void setupListeners() {
        binding.ivProfileSearch.setOnClickListener(v -> 
                startActivity(new Intent(ProfileActivity.this, SearchActivity.class)));

        binding.profileContainer.setOnClickListener(v -> 
                startActivity(new Intent(ProfileActivity.this, ProfileSelectionActivity.class)));

        binding.btnSeeDownloads.setOnClickListener(v -> 
                startActivity(new Intent(ProfileActivity.this, MainActivity.class)));

        binding.cvDownloads.setOnClickListener(v -> 
                startActivity(new Intent(ProfileActivity.this, DownloadsActivity.class)));

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

    private void loadDownloads() {
        String profileId = preferenceManager.getSelectedProfileId();
        if (profileId != null) {
            downloadRepository.getDownloadsForProfile(profileId, (success, downloads, e) -> {
                if (success && downloads != null && !downloads.isEmpty()) {
                    binding.rvDownloads.setVisibility(View.VISIBLE);
                    binding.btnSeeDownloads.setVisibility(View.GONE);
                    updateDownloadsPreview(downloads);
                } else {
                    binding.rvDownloads.setVisibility(View.GONE);
                    binding.btnSeeDownloads.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void loadMyList() {
        String profileId = preferenceManager.getSelectedProfileId();
        if (profileId != null) {
            userListRepository.getMyList(profileId, (success, movies, e) -> {
                if (success && movies != null) {
                    if (movies.isEmpty()) {
                        binding.rvMyList.setVisibility(View.GONE);
                        binding.tvEmptyMyList.setVisibility(View.VISIBLE);
                    } else {
                        myListAdapter.setMovies(movies);
                        binding.rvMyList.setVisibility(View.VISIBLE);
                        binding.tvEmptyMyList.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void updateDownloadsPreview(List<Download> downloads) {
        List<Movie> moviePreviews = new ArrayList<>();
        for (Download d : downloads) {
            Movie m = new Movie();
            m.setId(d.getMovieId());
            m.setTitle(d.getTitle());
            String path = d.getPosterPath();
            if (path == null || path.isEmpty()) {
                path = d.getBackdropPath();
            }
            if (path != null && !path.startsWith("/")) {
                path = "/" + path;
            }
            m.setPosterPath(path);
            m.setBackdropPath(d.getBackdropPath());
            moviePreviews.add(m);
        }
        downloadAdapter.setMovies(moviePreviews);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (binding.bottomNavBar != null) {
            binding.bottomNavBar.setSelectedItemId(R.id.nav_profile);
        }
        loadDownloads();
        loadMyList();
    }
}
