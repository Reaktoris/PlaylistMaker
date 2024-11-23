package com.practicum.playlistmaker.settings.ui.view_model

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.creator.Creator

class SettingsViewModel : ViewModel() {

    private val sharingInteractor = Creator.provideSharingInteractor()
    private val settingsInteractor = Creator.provideSettingsInteractor()

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