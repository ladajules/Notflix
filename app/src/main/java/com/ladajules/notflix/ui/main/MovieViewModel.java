package com.ladajules.notflix.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ladajules.notflix.data.model.Movie;
import com.ladajules.notflix.data.repository.TMDBRepository;
import com.ladajules.notflix.utils.Constants;

import java.util.List;

public class MovieViewModel extends ViewModel {
    private final TMDBRepository repository;
    private final LiveData<List<Movie>> popularMovies;
    private final LiveData<List<Movie>> trendingMovies;
    private final LiveData<List<Movie>> topRatedMovies;

    public MovieViewModel() {
        this.repository = new TMDBRepository(Constants.TMDB_API_KEY);
        this.popularMovies = repository.getPopularMovies(1);
        this.trendingMovies = repository.getTrendingMovies();
        this.topRatedMovies = repository.getTopRatedMovies(1);
    }

    public LiveData<List<Movie>> getPopularMovies() {
        return popularMovies;
    }

    public LiveData<List<Movie>> getTrendingMovies() {
        return trendingMovies;
    }

    public LiveData<List<Movie>> getTopRatedMovies() {
        return topRatedMovies;
    }

    public LiveData<List<Movie>> searchMovies(String query) {
        return repository.searchMovies(query, 1);
    }
}
