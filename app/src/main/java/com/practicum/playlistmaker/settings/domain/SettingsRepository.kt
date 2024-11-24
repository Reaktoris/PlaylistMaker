package com.practicum.playlistmaker.settings.domain

interface SettingsRepository {
    fun getThemeSettings(): Boolean

    fun updateThemeSetting(isNightMode: Boolean)
}