package com.test.appsfactorytask.common.util

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

fun Activity.hideKeyboard() {
    ContextCompat
        .getSystemService(this, InputMethodManager::class.java)
        ?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}