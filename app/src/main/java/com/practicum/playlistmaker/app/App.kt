package com.practicum.playlistmaker.app

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator

const val PREFERENCES = "playlist_maker_preferences"
const val NIGHT_MODE_KEY = "night_mode_key"
const val SEARCHED_TRACKS_KEY = "searched_tracks"

class App: Application() {

    private var darkTheme = false
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate() {
        super.onCreate()

        Creator.setApplication(this)

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