package com.ladajules.notflix.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ladajules.notflix.R;
import com.ladajules.notflix.adapter.MovieAdapter;
import com.ladajules.notflix.data.model.Movie;
import com.ladajules.notflix.data.repository.ProfileRepository;
import com.ladajules.notflix.databinding.ActivityMainBinding;
import com.ladajules.notflix.ui.profile.ProfileActivity;
import com.ladajules.notflix.ui.profile.ProfileSelectionActivity;
import com.ladajules.notflix.ui.search.SearchActivity;
import com.ladajules.notflix.utils.PreferenceManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MovieViewModel viewModel;
    private PreferenceManager preferenceManager;
    private ProfileRepository profileRepository;

    private MovieAdapter popularAdapter;
    private MovieAdapter trendingAdapter;
    private MovieAdapter topRatedAdapter;
    private MovieAdapter newReleasesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        profileRepository = new ProfileRepository();
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        initViews();
        setupAdapters();
        setupObservers();
        setupListeners();
        loadUserProfile();
    }

    private void initViews() {
        // Any specific view initialization not handled by binding
    }

    private void setupAdapters() {
        popularAdapter = new MovieAdapter(this::onMovieClick);
        trendingAdapter = new MovieAdapter(this::onMovieClick);
        topRatedAdapter = new MovieAdapter(this::onMovieClick);
        newReleasesAdapter = new MovieAdapter(this::onMovieClick);

        binding.rvContinueWatching.setAdapter(popularAdapter);
        binding.rvTrending.setAdapter(trendingAdapter);
        binding.rvTop10.setAdapter(topRatedAdapter);
        binding.rvNewReleases.setAdapter(newReleasesAdapter);
        binding.rvBecauseYouWatched.setAdapter(popularAdapter); // Reuse for demo
    }

    private void setupObservers() {
        viewModel.getPopularMovies().observe(this, movies -> {
            if (movies != null) {
                popularAdapter.setMovies(movies);
                updateHeroBanner(movies.get(0));
            }
        });

        viewModel.getTrendingMovies().observe(this, movies -> {
            if (movies != null) {
                trendingAdapter.setMovies(movies);
            }
        });

        viewModel.getTopRatedMovies().observe(this, movies -> {
            if (movies != null) {
                topRatedAdapter.setMovies(movies);
                newReleasesAdapter.setMovies(movies); // Mocking new releases
            }
        });
    }

    private void updateHeroBanner(Movie movie) {
        binding.tvHeroTitle.setText(movie.getTitle());
        Glide.with(this)
                .load(movie.getFullBackdropPath())
                .into(binding.ivHeroBanner);
    }

    private void setupListeners() {
        binding.searchBar.setOnClickListener(v -> 
                startActivity(new Intent(MainActivity.this, SearchActivity.class)));

        binding.ivProfileIcon.setOnClickListener(v -> 
                startActivity(new Intent(MainActivity.this, ProfileActivity.class)));

        binding.bottomNavBar.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                return false;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
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
                    setProfileAvatar(binding.ivProfileIcon, profile.getAvatarUrl());
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

    private void onMovieClick(Movie movie) {
        // TODO: Implement movie details screen
        Log.d("MainActivity", "Clicked: " + movie.getTitle());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (binding.bottomNavBar != null) {
            binding.bottomNavBar.setSelectedItemId(R.id.nav_home);
        }
        loadUserProfile();
    }
}
