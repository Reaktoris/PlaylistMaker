package com.practicum.playlistmaker.settings.data.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.app.NIGHT_MODE_KEY
import com.practicum.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) : SettingsRepository {
    override fun getThemeSettings(): Boolean {
        return sharedPreferences.getBoolean(NIGHT_MODE_KEY, true)
    }

    override fun updateThemeSetting(isNightMode: Boolean) {
        sharedPreferences.edit().putBoolean(NIGHT_MODE_KEY, isNightMode).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (isNightMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}