package com.ladajules.notflix.ui.details;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.ladajules.notflix.R;
import com.ladajules.notflix.adapter.MovieAdapter;
import com.ladajules.notflix.data.model.CreditsResponse;
import com.ladajules.notflix.data.model.Movie;
import com.ladajules.notflix.data.repository.DownloadRepository;
import com.ladajules.notflix.data.repository.UserListRepository;
import com.ladajules.notflix.databinding.ActivityDetailsBinding;
import com.ladajules.notflix.data.model.MovieViewModel;
import com.ladajules.notflix.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";

    private ActivityDetailsBinding binding;
    private Movie movie;
    private MovieViewModel viewModel;
    private MovieAdapter moreLikeThisAdapter;
    private DownloadRepository downloadRepository;
    private UserListRepository userListRepository;
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
        userListRepository = new UserListRepository();
        preferenceManager = new PreferenceManager(this);

        setupViews();
        setupMoreLikeThis();
        setupListeners();
        fetchCredits();
    }

    private void setupViews() {
        binding.tvMovieTitle.setText(movie.getTitle());
        binding.tvSynopsis.setText(movie.getOverview());
        
        Glide.with(this)
                .load(movie.getFullBackdropPath())
                .placeholder(R.drawable.profile_avatar_background)
                .into(binding.ivPoster);
    }

    private void fetchCredits() {
        viewModel.getMovieCredits(movie.getId()).observe(this, credits -> {
            if (credits != null) {
                updateCreditsUI(credits);
            }
        });
    }

    private void updateCreditsUI(CreditsResponse credits) {
        List<String> castNames = new ArrayList<>();
        if (credits.getCast() != null) {
            int count = Math.min(credits.getCast().size(), 3);
            for (int i = 0; i < count; i++) {
                castNames.add(credits.getCast().get(i).getName());
            }
        }
        String castText = "Cast: " + String.join(", ", castNames);
        if (credits.getCast() != null && credits.getCast().size() > 3) {
            castText += " ... more";
        }

        String directorName = "N/A";
        if (credits.getCrew() != null) {
            for (CreditsResponse.Crew crewMember : credits.getCrew()) {
                if ("Director".equals(crewMember.getJob())) {
                    directorName = crewMember.getName();
                    break;
                }
            }
        }

        binding.tvCastInfo.setText(castText + "\nDirector: " + directorName);
    }

    private void setupMoreLikeThis() {
        moreLikeThisAdapter = new MovieAdapter(clickedMovie -> {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(EXTRA_MOVIE, clickedMovie);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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

        View playLayout = binding.cvPlayBtn.getChildAt(0);
        if (playLayout != null) {
            playLayout.setOnClickListener(v -> {
                Toast.makeText(this, "Now playing " + movie.getTitle(), Toast.LENGTH_SHORT).show();
            });
        }

        View downloadLayout = binding.cvDownloadBtn.getChildAt(0);
        if (downloadLayout != null) {
            downloadLayout.setOnClickListener(v -> {
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
        }

        binding.llAddToList.setOnClickListener(v -> {
            String profileId = preferenceManager.getSelectedProfileId();
            if (profileId != null) {
                userListRepository.addToMyList(profileId, movie, (success, data, e) -> {
                    if (success) {
                        Toast.makeText(this, "Added " + movie.getTitle() + " to your list", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to add to list", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Please select a profile first", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
