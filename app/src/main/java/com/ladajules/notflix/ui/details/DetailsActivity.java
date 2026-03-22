package com.ladajules.notflix.ui.details;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.ladajules.notflix.R;
import com.ladajules.notflix.adapter.MovieAdapter;
import com.ladajules.notflix.data.model.Download;
import com.ladajules.notflix.data.model.Movie;
import com.ladajules.notflix.data.repository.DownloadRepository;
import com.ladajules.notflix.databinding.ActivityDetailsBinding;
import com.ladajules.notflix.ui.main.MovieViewModel;
import com.ladajules.notflix.utils.PreferenceManager;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";

    private ActivityDetailsBinding binding;
    private Movie movie;
    private MovieViewModel viewModel;
    private MovieAdapter moreLikeThisAdapter;
    private DownloadRepository downloadRepository;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        if (movie == null) {
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        downloadRepository = new DownloadRepository();
        preferenceManager = new PreferenceManager(this);

        setupViews();
        setupMoreLikeThis();
        setupListeners();
    }

    private void setupViews() {
        binding.tvMovieTitle.setText(movie.getTitle());
        binding.tvSynopsis.setText(movie.getOverview());
        
        // Mocking some data that isn't in the Movie model yet
        String releaseYear = movie.getReleaseDate() != null && movie.getReleaseDate().length() >= 4 
                ? movie.getReleaseDate().substring(0, 4) : "N/A";
        
        Glide.with(this)
                .load(movie.getFullBackdropPath())
                .placeholder(R.drawable.profile_avatar_background)
                .into(binding.ivPoster);
    }

    private void setupMoreLikeThis() {
        moreLikeThisAdapter = new MovieAdapter(clickedMovie -> {
            // Re-open DetailsActivity with the new movie
            // You might want to use intent flags to manage the backstack
        }, true);

        binding.rvMoreLikeThis.setLayoutManager(new GridLayoutManager(this, 3));
        binding.rvMoreLikeThis.setAdapter(moreLikeThisAdapter);

        viewModel.getPopularMovies().observe(this, movies -> {
            if (movies != null) {
                moreLikeThisAdapter.setMovies(movies);
            }
        });
    }

    private void setupListeners() {
        binding.ivClose.setOnClickListener(v -> finish());

        binding.cvPlayBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Now playing " + movie.getTitle(), Toast.LENGTH_SHORT).show();
        });

        binding.cvDownloadBtn.setOnClickListener(v -> {
            String profileId = preferenceManager.getSelectedProfileId();
            if (profileId != null) {
                downloadRepository.addDownload(profileId, movie, (success, data, e) -> {
                    if (success) {
                        Toast.makeText(this, "Added to Downloads", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to add to Downloads", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.llAddToList.setOnClickListener(v -> {
            // TODO: Implement My List logic
            Toast.makeText(this, "Added to My List", Toast.LENGTH_SHORT).show();
        });
    }
}
