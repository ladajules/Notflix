package com.ladajules.notflix.ui.landing;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.tabs.TabLayoutMediator;
import com.ladajules.notflix.adapter.LandingPagerAdapter;
import com.ladajules.notflix.databinding.ActivityLandingBinding;
import com.ladajules.notflix.ui.auth.SignInActivity;
import com.ladajules.notflix.ui.auth.SignupActivity;

public class LandingActivity extends AppCompatActivity {

    private ActivityLandingBinding binding;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable autoScrollRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLandingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupEdgeToEdge();
        setupViewPager();
        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAutoScroll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAutoScroll();
    }

    private void setupEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        WindowInsetsControllerCompat windowInsetsController = 
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
    }

    private void setupViewPager() {
        LandingPagerAdapter adapter = new LandingPagerAdapter();
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayoutLanding, binding.viewPager, (tab, position) -> {
            // No text needed for dots
        }).attach();
    }

    private void setupClickListeners() {
        binding.btnSignIn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignInActivity.class));
        });

        binding.btnCreateAccount.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
    }

    private void startAutoScroll() {
        stopAutoScroll();
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = binding.viewPager.getCurrentItem();
                int nextItem = (currentItem + 1) % 4;
                binding.viewPager.setCurrentItem(nextItem, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(autoScrollRunnable, 3000);
    }

    private void stopAutoScroll() {
        if (autoScrollRunnable != null) {
            handler.removeCallbacks(autoScrollRunnable);
        }
        autoScrollRunnable = null;
    }
}
