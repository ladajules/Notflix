package com.ladajules.notflix.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import android.util.Patterns;

public class Extensions {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void show(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hide(View view) {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    public static void invisible(View view) {
        if (view != null) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static void enable(View view) {
        if (view != null) {
            view.setEnabled(true);
        }
    }

    public static void disable(View view) {
        if (view != null) {
            view.setEnabled(false);
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Activity activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static int isValidPassword(String password) {
        if (password == null || password.length() < Constants.MIN_PASSWORD_LENGTH) return 0;

        int strength = 0;
        if (password.length() >= 8) strength++;
        
        boolean hasDigit = false;
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) hasDigit = true;
            else if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }

        if (hasDigit) strength++;
        if (hasUpper) strength++;
        if (hasLower) strength++;
        if (hasSpecial) strength++;

        if (strength <= 2) return 0;
        if (strength <= 4) return 1;
        return 2;
    }

    public static int getColorCompat(Context context, int colorRes) {
        return ContextCompat.getColor(context, colorRes);
    }
}
