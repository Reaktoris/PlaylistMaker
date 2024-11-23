package com.practicum.playlistmaker.settings.data.impl

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.app.NIGHT_MODE_KEY
import com.practicum.playlistmaker.app.PREFERENCES
import com.practicum.playlistmaker.settings.data.SettingsRepository

class SettingsRepositoryImpl(context: Context) : SettingsRepository {
    private val sharedPref = context.getSharedPreferences(PREFERENCES, Application.MODE_PRIVATE)
    override fun getThemeSettings(): Boolean {
        return sharedPref.getBoolean(NIGHT_MODE_KEY, true)
    }

    override fun updateThemeSetting(isNightMode: Boolean) {
        sharedPref.edit().putBoolean(NIGHT_MODE_KEY, isNightMode).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (isNightMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}