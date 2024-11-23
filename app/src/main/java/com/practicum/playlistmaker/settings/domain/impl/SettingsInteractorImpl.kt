package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.data.SettingsRepository
import com.practicum.playlistmaker.settings.domain.SettingsInteractor

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) : SettingsInteractor {
    override fun getThemeSettings(): Boolean {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSetting(isNightMode: Boolean) {
        settingsRepository.updateThemeSetting(isNightMode)
    }
}