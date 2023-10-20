package com.example.jarvishome.core.common

import android.content.Context
import android.content.SharedPreferences

class Preferences(context:Context) {

    companion object {
        private const val SHARED_PREFS = "Shared_prefs"
    }
    private val preferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE)
}