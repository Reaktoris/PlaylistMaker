package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.impl.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.network.ITunesSearchApiService
import com.practicum.playlistmaker.search.data.network.NetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    factory {
        Handler(Looper.getMainLooper())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<ITunesSearchApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesSearchApiService::class.java)
    }

    factory {
        Gson()
    }

    single {
        androidContext().getSharedPreferences("playlist_maker_preferences", Context.MODE_PRIVATE)
    }

    factory {
        MediaPlayer()
    }
}