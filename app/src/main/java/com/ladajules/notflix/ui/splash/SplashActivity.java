package com.ladajules.notflix.ui.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.ladajules.notflix.R;
import com.ladajules.notflix.ui.landing.LandingActivity;
import com.ladajules.notflix.ui.profile.ProfileSelectionActivity;
import com.ladajules.notflix.utils.Constants;
import com.ladajules.notflix.utils.PreferenceManager;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preferenceManager = new PreferenceManager(this);

        setupFullscreen();

        navigateToNextScreen();
    }

    private void setupFullscreen() {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }

    private void navigateToNextScreen() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent;

            if (preferenceManager.isLoggedIn() && preferenceManager.isRememberMe()) {
                intent = new Intent(this, ProfileSelectionActivity.class);
            } else {
                intent = new Intent(this, LandingActivity.class);
            }

            startActivity(intent);
            finish();

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }, Constants.SPLASH_DELAY);
    }
}
