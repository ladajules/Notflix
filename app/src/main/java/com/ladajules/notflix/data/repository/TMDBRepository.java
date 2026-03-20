package com.ladajules.notflix.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ladajules.notflix.data.model.Movie;
import com.ladajules.notflix.data.model.MovieResponse;
import com.ladajules.notflix.data.remote.RetrofitClient;
import com.ladajules.notflix.data.remote.TMDBApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TMDBRepository {
    private static final String TAG = "TMDBRepository";
    private final TMDBApiService apiService;
    private final String apiKey;

    public TMDBRepository(String apiKey) {
        this.apiService = RetrofitClient.getTMDBService();
        this.apiKey = apiKey;
    }

    public LiveData<List<Movie>> getPopularMovies(int page) {
        MutableLiveData<List<Movie>> data = new MutableLiveData<>();
        apiService.getPopularMovies(apiKey, "en-US", page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body().getResults());
                } else {
                    Log.e(TAG, "Failed to get popular movies: " + response.code());
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, "Error getting popular movies", t);
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<Movie>> getTrendingMovies() {
        MutableLiveData<List<Movie>> data = new MutableLiveData<>();
        apiService.getTrendingMovies(apiKey).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body().getResults());
                } else {
                    Log.e(TAG, "Failed to get trending movies: " + response.code());
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, "Error getting trending movies", t);
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<Movie>> getTopRatedMovies(int page) {
        MutableLiveData<List<Movie>> data = new MutableLiveData<>();
        apiService.getTopRatedMovies(apiKey, "en-US", page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body().getResults());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<Movie>> searchMovies(String query, int page) {
        MutableLiveData<List<Movie>> data = new MutableLiveData<>();
        apiService.searchMovies(apiKey, query, "en-US", page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body().getResults());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
