package com.practicum.playlistmaker.settings.ui.view_model

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun getThemeSettings(): Boolean {
        return settingsInteractor.getThemeSettings()
    }

    fun updateThemeSettings(isNightMode: Boolean) {
        settingsInteractor.updateThemeSetting(isNightMode)
    }
}