package com.ladajules.notflix.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ladajules.notflix.R;
import com.ladajules.notflix.adapter.MovieAdapter;
import com.ladajules.notflix.data.model.Movie;
import com.ladajules.notflix.data.model.MovieViewModel;
import com.ladajules.notflix.data.repository.ProfileRepository;
import com.ladajules.notflix.data.repository.UserListRepository;
import com.ladajules.notflix.databinding.ActivityMainBinding;
import com.ladajules.notflix.databinding.DialogMysteryTicketBinding;
import com.ladajules.notflix.ui.details.DetailsActivity;
import com.ladajules.notflix.ui.download.DownloadsActivity;
import com.ladajules.notflix.ui.profile.ProfileActivity;
import com.ladajules.notflix.ui.search.SearchActivity;
import com.ladajules.notflix.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MovieViewModel viewModel;
    private PreferenceManager preferenceManager;
    private ProfileRepository profileRepository;
    private UserListRepository userListRepository;

    private MovieAdapter continueWatchingAdapter;
    private MovieAdapter top10Adapter;
    private MovieAdapter newReleasesAdapter;
    private MovieAdapter trendingAdapter;
    private MovieAdapter becauseYouWatchedAdapter;

    private List<Movie> allMovies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        profileRepository = new ProfileRepository();
        userListRepository = new UserListRepository();
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        setupAdapters();
        setupObservers();
        setupListeners();
        loadUserProfile();
    }

    private void setupAdapters() {
        continueWatchingAdapter = new MovieAdapter(this::onMovieClick);
        top10Adapter = new MovieAdapter(this::onMovieClick);
        newReleasesAdapter = new MovieAdapter(this::onMovieClick);
        trendingAdapter = new MovieAdapter(this::onMovieClick);
        becauseYouWatchedAdapter = new MovieAdapter(this::onMovieClick);

        binding.rvContinueWatching.setAdapter(continueWatchingAdapter);
        binding.rvTop10.setAdapter(top10Adapter);
        binding.rvNewReleases.setAdapter(newReleasesAdapter);
        binding.rvTrending.setAdapter(trendingAdapter);
        binding.rvBecauseYouWatched.setAdapter(becauseYouWatchedAdapter);
    }

    private void setupObservers() {
        viewModel.getPopularMovies().observe(this, movies -> {
            if (movies != null && !movies.isEmpty()) {
                allMovies.addAll(movies);
                List<Movie> shuffled = new ArrayList<>(movies);
                Collections.shuffle(shuffled);
                continueWatchingAdapter.setMovies(shuffled);
                updateHeroBanner(movies.get(0));
            }
        });

        viewModel.getTopRatedMovies().observe(this, movies -> {
            if (movies != null && !movies.isEmpty()) {
                allMovies.addAll(movies);
                List<Movie> top10 = movies.subList(0, Math.min(movies.size(), 10));
                top10Adapter.setMovies(top10);
            }
        });

        viewModel.getNowPlayingMovies().observe(this, movies -> {
            if (movies != null) {
                allMovies.addAll(movies);
                newReleasesAdapter.setMovies(movies);
            }
        });

        viewModel.getTrendingMovies().observe(this, movies -> {
            if (movies != null) {
                allMovies.addAll(movies);
                trendingAdapter.setMovies(movies);
            }
        });

        viewModel.getUpcomingMovies().observe(this, movies -> {
            if (movies != null && !movies.isEmpty()) {
                allMovies.addAll(movies);
                List<Movie> shuffled = new ArrayList<>(movies);
                Collections.shuffle(shuffled);
                becauseYouWatchedAdapter.setMovies(shuffled);
            }
        });
    }

    private void updateHeroBanner(Movie movie) {
        binding.tvHeroTitle.setText(movie.getTitle());
        Glide.with(this)
                .load(movie.getFullBackdropPath())
                .into(binding.ivHeroBanner);

        binding.btnMyList.setOnClickListener(v -> {
            String profileId = preferenceManager.getSelectedProfileId();
            if (profileId != null) {
                userListRepository.addToMyList(profileId, movie, (success, data, e) -> {
                    if (success) {
                        Toast.makeText(this, "Added " + movie.getTitle() + " to your list", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to add to list", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        
        binding.btnPlay.setOnClickListener(v -> Toast.makeText(this, "Now playing " + movie.getTitle(), Toast.LENGTH_SHORT).show());
        binding.btnInfo.setOnClickListener(v -> onMovieClick(movie));
    }

    private void setupListeners() {
        binding.searchBar.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SearchActivity.class)));
        binding.ivProfileIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProfileActivity.class)));
        binding.btnMysteryTicket.setOnClickListener(v -> showMysteryTicketDialog());

        binding.bottomNavBar.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) return true;
            if (itemId == R.id.nav_search) { startActivity(new Intent(MainActivity.this, SearchActivity.class)); return false; }
            if (itemId == R.id.nav_downloads) { startActivity(new Intent(MainActivity.this, DownloadsActivity.class)); return false; }
            if (itemId == R.id.nav_profile) { startActivity(new Intent(MainActivity.this, ProfileActivity.class)); return false; }
            return true;
        });
    }

    private void showMysteryTicketDialog() {
        DialogMysteryTicketBinding dialogBinding = DialogMysteryTicketBinding.inflate(getLayoutInflater());
        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setView(dialogBinding.getRoot())
                .create();

        String[] genres = {"Action", "Comedy", "Drama", "Horror", "Sci-Fi", "Animation"};
        String[] durations = {"Short (< 90m)", "Medium (90-120m)", "Long (> 120m)"};
        String[] years = {"2026", "2025", "2024", "2023", "2022", "2021", "2020", "Older"};

        dialogBinding.actvGenre.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, genres));
        dialogBinding.actvDuration.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, durations));
        dialogBinding.actvReleaseDate.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, years));

        dialogBinding.btnGenerate.setOnClickListener(v -> {
            if (allMovies.isEmpty()) {
                Toast.makeText(this, "Loading movies, please wait...", Toast.LENGTH_SHORT).show();
                return;
            }

            Movie randomMovie = allMovies.get(new Random().nextInt(allMovies.size()));
            
            dialogBinding.llResult.setVisibility(View.VISIBLE);
            dialogBinding.tvResultTitle.setText(randomMovie.getTitle());
            Glide.with(this).load(randomMovie.getFullPosterPath()).into(dialogBinding.ivResultPoster);
            
            dialogBinding.btnMysteryPlay.setOnClickListener(v2 -> {
                Toast.makeText(this, "Now playing " + randomMovie.getTitle(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private void loadUserProfile() {
        String profileId = preferenceManager.getSelectedProfileId();
        if (profileId != null) {
            profileRepository.getProfile(profileId, (success, profile, e) -> {
                if (success && profile != null) setProfileAvatar(binding.ivProfileIcon, profile.getAvatarUrl());
            });
        }
    }

    private void setProfileAvatar(ImageView imageView, String avatarUrl) {
        int avatarRes = R.drawable.avatar_default;
        if (avatarUrl != null) {
            switch (avatarUrl.toLowerCase()) {
                case "pink": avatarRes = R.drawable.avatar_pink; break;
                case "green": avatarRes = R.drawable.avatar_green; break;
                case "orange": avatarRes = R.drawable.avatar_orange; break;
                case "yellow": avatarRes = R.drawable.avatar_yellow; break;
                case "blue": avatarRes = R.drawable.avatar_blue; break;
            }
        }
        imageView.setImageResource(avatarRes);
    }

    private void onMovieClick(Movie movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (binding.bottomNavBar != null) binding.bottomNavBar.setSelectedItemId(R.id.nav_home);
        loadUserProfile();
    }
}
