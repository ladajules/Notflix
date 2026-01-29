package com.ladajules.notflix.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE)

    // User Authentication
    var isLoggedIn: Boolean
        get() = sharedPreferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false)
        set(value) = sharedPreferences.edit().putBoolean(Constants.KEY_IS_LOGGED_IN, value).apply()

    var userId: String?
        get() = sharedPreferences.getString(Constants.KEY_USER_ID, null)
        set(value) = sharedPreferences.edit().putString(Constants.KEY_USER_ID, value).apply()

    var rememberMe: Boolean
        get() = sharedPreferences.getBoolean(Constants.KEY_REMEMBER_ME, false)
        set(value) = sharedPreferences.edit().putBoolean(Constants.KEY_REMEMBER_ME, value).apply()

    // Onboarding
    var onboardingCompleted: Boolean
        get() = sharedPreferences.getBoolean(Constants.KEY_ONBOARDING_COMPLETED, false)
        set(value) = sharedPreferences.edit().putBoolean(Constants.KEY_ONBOARDING_COMPLETED, value).apply()

    // Profile
    var selectedProfileId: String?
        get() = sharedPreferences.getString(Constants.KEY_SELECTED_PROFILE_ID, null)
        set(value) = sharedPreferences.edit().putString(Constants.KEY_SELECTED_PROFILE_ID, value).apply()

    // Clear all preferences on logout
    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }

    // Clear user session but keep onboarding status
    fun clearSession() {
        isLoggedIn = false
        userId = null
        selectedProfileId = null
        rememberMe = false
    }
}