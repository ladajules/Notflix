package com.ladajules.notflix.utils;

public class Constants {

    // Firebase collections
    public static final String USERS_COLLECTION = "users";
    public static final String PROFILES_COLLECTION = "profiles";

    // SharedPreferences
    public static final String PREFERENCES_NAME = "notflix_preferences";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_REMEMBER_ME = "remember_me";
    public static final String KEY_ONBOARDING_COMPLETED = "onboarding_completed";
    public static final String KEY_SELECTED_PROFILE_ID = "selected_profile_id";

    // TMDB API
    public static final String TMDB_API_KEY = "a05f6ebaf75c1f59f5b21bd6653b0cc1";
    public static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    public static final String TMDB_IMAGE_SIZE_ORIGINAL = "original";
    public static final String TMDB_IMAGE_SIZE_W500 = "w500";
    public static final String TMDB_IMAGE_SIZE_W780 = "w780";
    public static final String TMDB_IMAGE_SIZE_W1280 = "w1280";

    // Movie categories
    public static final String CATEGORY_TOP_RATED = "top_rated";
    public static final String CATEGORY_POPULAR = "popular";
    public static final String CATEGORY_NOW_PLAYING = "now_playing";
    public static final String CATEGORY_UPCOMING = "upcoming";

    // Profile limit
    public static final int MAX_PROFILES_PER_USER = 5;

    // Password validation
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_NAME_LENGTH = 50;

    // Delays
    public static final long SPLASH_DELAY = 2000L;
    public static final long TYPING_DELAY = 300L;

    // Private constructor to prevent instantiation
    private Constants() {}
}
