package com.ladajules.notflix.ui.onboarding;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;
import com.ladajules.notflix.R;
import com.ladajules.notflix.adapter.OnboardingAdapter;
import com.ladajules.notflix.data.model.OnboardingItem;
import com.ladajules.notflix.data.repository.UserRepository;
import com.ladajules.notflix.databinding.ActivityOnboardingBinding;
import com.ladajules.notflix.ui.profile.ProfileSelectionActivity;
import com.ladajules.notflix.utils.Extensions;
import com.ladajules.notflix.utils.PreferenceManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnboardingActivity extends AppCompatActivity {

    private ActivityOnboardingBinding binding;
    private PreferenceManager preferenceManager;
    private UserRepository userRepository;

    private final List<OnboardingItem> onboardingItems = Arrays.asList(
            new OnboardingItem(
                    "Unlimited entertainment, one low price",
                    "All of Notflix, starting at just ₱149. No extra costs, no contracts.",
                    R.drawable.onboarding_1
            ),
            new OnboardingItem(
                    "Download and go",
                    "Save your favorites easily and always have something to watch.",
                    R.drawable.onboarding_2
            ),
            new OnboardingItem(
                    "No ads, no interruptions",
                    "Watch anywhere, anytime on an unlimited number of devices.",
                    R.drawable.onboarding_3
            ),
            new OnboardingItem(
                    "Every kind of movie and TV show",
                    "Watch on Smart TVs, PlayStation, Xbox, Chromecast, Apple TV and more.",
                    R.drawable.onboarding_4
            )
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        userRepository = new UserRepository();

        setupUI();
        setupViewPager();
        setupListeners();
    }

    private void setupUI() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    private void setupViewPager() {
        OnboardingAdapter onboardingAdapter = new OnboardingAdapter(onboardingItems);
        binding.viewPager.setAdapter(onboardingAdapter);

        new TabLayoutMediator(binding.tabLayoutOnboarding, binding.viewPager, (tab, position) -> {
        }).attach();

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateButtonText(position);
            }
        });
    }

    private void setupListeners() {
        binding.btnNext.setOnClickListener(v -> {
            int currentItem = binding.viewPager.getCurrentItem();
            if (currentItem < onboardingItems.size() - 1) {
                binding.viewPager.setCurrentItem(currentItem + 1);
            } else {
                finishOnboarding();
            }
        });
    }

    private void updateButtonText(int position) {
        if (position == onboardingItems.size() - 1) {
            binding.btnNext.setText("Get Started");
        } else {
            binding.btnNext.setText("Next");
        }
    }

    private void finishOnboarding() {
        String userId = preferenceManager.getUserId();

        if (userId != null) {
            Map<String, Object> updates = new HashMap<>();
            updates.put("hasCompletedOnboarding", true);

            userRepository.updateUser(userId, updates, (success, data, e) -> {
                if (success) {
                    preferenceManager.setOnboardingCompleted(true);
                    navigateToProfileSelectionActivity();
                } else {
                    Extensions.showToast(this, "Failed to update: " + (e != null ? e.getMessage() : "Unknown error"));
                }
            });
        } else {
            preferenceManager.setOnboardingCompleted(true);
            navigateToProfileSelectionActivity();
        }
    }

    private void navigateToProfileSelectionActivity() {
        Intent intent = new Intent(this, ProfileSelectionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
