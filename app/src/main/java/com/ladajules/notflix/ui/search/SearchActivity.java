package com.ladajules.notflix.ui.search;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ladajules.notflix.R;
import com.ladajules.notflix.adapter.MovieAdapter;
import com.ladajules.notflix.data.model.Movie;
import com.ladajules.notflix.data.model.MovieViewModel;

public class SearchActivity extends AppCompatActivity {

    private ImageView ivBack;
    private EditText etSearch;
    private ImageView ivClearSearch;
    private TextView tvCancel;
    private RecyclerView rvSearchResults;
    
    private MovieViewModel viewModel;
    private MovieAdapter searchAdapter;
    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        initViews();
        setupRecyclerView();
        setupListeners();
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        etSearch = findViewById(R.id.etSearch);
        ivClearSearch = findViewById(R.id.ivClearSearch);
        tvCancel = findViewById(R.id.tvCancel);
        rvSearchResults = findViewById(R.id.rvSearchResults);
    }

    private void setupRecyclerView() {
        searchAdapter = new MovieAdapter(this::onMovieClick, true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        rvSearchResults.setLayoutManager(layoutManager);
        rvSearchResults.setAdapter(searchAdapter);
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> finish());

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                ivClearSearch.setVisibility(query.isEmpty() ? View.GONE : View.VISIBLE);

                searchHandler.removeCallbacks(searchRunnable);
                searchRunnable = () -> {
                    if (!query.isEmpty()) {
                        performSearch(query);
                    } else {
                        searchAdapter.setMovies(null);
                    }
                };
                searchHandler.postDelayed(searchRunnable, 500);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = etSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    performSearch(query);
                }
                return true;
            }
            return false;
        });

        ivClearSearch.setOnClickListener(v -> {
            etSearch.getText().clear();
            searchAdapter.setMovies(null);
            etSearch.requestFocus();
        });

        tvCancel.setOnClickListener(v -> finish());
    }

    private void performSearch(String query) {
        viewModel.searchMovies(query).observe(this, movies -> {
            if (movies != null) {
                searchAdapter.setMovies(movies);
            }
        });
    }

    private void onMovieClick(Movie movie) {
        // TODO: Implement movie details screen
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchHandler.removeCallbacks(searchRunnable);
    }
}
