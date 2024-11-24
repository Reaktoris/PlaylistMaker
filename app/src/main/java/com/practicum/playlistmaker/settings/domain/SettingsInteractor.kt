package com.practicum.playlistmaker.settings.domain

interface SettingsInteractor {
    fun getThemeSettings(): Boolean

    fun updateThemeSetting(isNightMode: Boolean)
}