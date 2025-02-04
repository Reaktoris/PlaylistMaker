package com.practicum.playlistmaker.app

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.markodevcic.peko.PermissionRequester
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

const val PREFERENCES = "playlist_maker_preferences"
const val NIGHT_MODE_KEY = "night_mode_key"
const val SEARCHED_TRACKS_KEY = "searched_tracks"
const val FILE_NAME_KEY = "file_name"

class App: Application() {

    private var darkTheme = false
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

        PermissionRequester.initialize(applicationContext)

        sharedPref = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPref.getBoolean(
            NIGHT_MODE_KEY,
            true
        )


        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}