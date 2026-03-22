package com.ladajules.notflix.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private final SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean value) {
        sharedPreferences.edit().putBoolean(Constants.KEY_IS_LOGGED_IN, value).apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(Constants.KEY_USER_ID, null);
    }

    public void setUserId(String value) {
        sharedPreferences.edit().putString(Constants.KEY_USER_ID, value).apply();
    }

    public boolean isRememberMe() {
        return sharedPreferences.getBoolean(Constants.KEY_REMEMBER_ME, false);
    }

    public void setRememberMe(boolean value) {
        sharedPreferences.edit().putBoolean(Constants.KEY_REMEMBER_ME, value).apply();
    }

    // Onboarding
    public boolean isOnboardingCompleted() {
        return sharedPreferences.getBoolean(Constants.KEY_ONBOARDING_COMPLETED, false);
    }

    public void setOnboardingCompleted(boolean value) {
        sharedPreferences.edit().putBoolean(Constants.KEY_ONBOARDING_COMPLETED, value).apply();
    }

    // Profile
    public String getSelectedProfileId() {
        return sharedPreferences.getString(Constants.KEY_SELECTED_PROFILE_ID, null);
    }

    public void setSelectedProfileId(String value) {
        sharedPreferences.edit().putString(Constants.KEY_SELECTED_PROFILE_ID, value).apply();
    }

    public void clearAll() {
        sharedPreferences.edit().clear().apply();
    }

    // clear user session but keep onboarding status
    public void clearSession() {
        setLoggedIn(false);
        setUserId(null);
        setSelectedProfileId(null);
        setRememberMe(false);
    }

}
