package com.practicum.playlistmaker.settings.data

interface SettingsRepository {
    fun getThemeSettings(): Boolean

    fun updateThemeSetting(isNightMode: Boolean)
}