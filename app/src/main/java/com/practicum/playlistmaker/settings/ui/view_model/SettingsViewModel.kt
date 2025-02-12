package com.practicum.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val themeLiveData = MutableLiveData<Boolean>()

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun getThemeLiveData(): LiveData<Boolean> {
        return themeLiveData
    }

    fun updateThemeSettings(isNightMode: Boolean) {
        settingsInteractor.updateThemeSetting(isNightMode)
        themeLiveData.value = settingsInteractor.getThemeSettings()
    }
}