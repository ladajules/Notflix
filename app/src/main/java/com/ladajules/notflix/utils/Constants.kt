package com.ladajules.notflix.utils

object Constants {

    // Firebase collections
    const val USERS_COLLECTION = "users"
    const val PROFILES_COLLECTION ="profiles"

    // SharedPreferences
    const val PREFERENCES_NAME = "notflix_preferences"
    const val KEY_IS_LOGGED_IN = "is_logged_in"
    const val KEY_USER_ID = "user_id"
    const val KEY_REMEMBER_ME = "remember_me"
    const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    const val KEY_SELECTED_PROFILE_ID = "selected_profile_id"

    // TMDB API
    const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
    const val TMDB_IMAGE_SIZE_ORIGINAL = "original"
    const val TMDB_IMAGE_SIZE_W500 = "w500"
    const val TMDB_IMAGE_SIZE_W780 = "w780"
    const val TMDB_IMAGE_SIZE_W1280 = "w1280"

    // Movie categories
    const val CATEGORY_TOP_RATED = "top_rated"
    const val CATEGORY_POPULAR = "popular"
    const val CATEGORY_NOW_PLAYING = "now_playing"
    const val CATEGORY_UPCOMING = "upcoming"

    // Profile limit
    const val MAX_PROFILES_PER_USER = 5

    // Password validation
    const val MIN_PASSWORD_LENGTH = 6
    const val MAX_NAME_LENGTH = 50

    // Delays
    const val SPLASH_DELAY = 2000L
    const val TYPING_DELAY = 300L

}