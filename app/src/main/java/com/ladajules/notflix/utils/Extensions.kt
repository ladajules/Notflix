package com.ladajules.notflix.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = currentFocus ?: View(this)
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.showKeyboard(view: View) {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Int {
    if (this.length < Constants.MIN_PASSWORD_LENGTH) return 0

    var strength = 0
    if (this.length >= 8) strength++
    if (this.any { it.isDigit() }) strength++
    if (this.any { it.isUpperCase() }) strength++
    if (this.any { it.isLowerCase() }) strength++
    if (this.any { !it.isLetterOrDigit() }) strength++

    return when {
        strength <= 2 -> 0
        strength <= 4 -> 1
        else -> 2
    }
}

fun Context.getColorCompat(colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}
