package com.ladajules.notflix.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ladajules.notflix.R;

public class SearchActivity extends AppCompatActivity {

    private ImageView ivBack;
    private EditText etSearch;
    private ImageView ivClearSearch;
    private TextView tvCancel;
    private RecyclerView rvSearchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        setupListeners();
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        etSearch = findViewById(R.id.etSearch);
        ivClearSearch = findViewById(R.id.ivClearSearch);
        tvCancel = findViewById(R.id.tvCancel);
        rvSearchResults = findViewById(R.id.rvSearchResults);
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> finish());

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ivClearSearch.setVisibility(s == null || s.length() == 0 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ivClearSearch.setOnClickListener(v -> {
            etSearch.getText().clear();
            etSearch.requestFocus();
        });

        tvCancel.setOnClickListener(v -> finish());
    }
}
