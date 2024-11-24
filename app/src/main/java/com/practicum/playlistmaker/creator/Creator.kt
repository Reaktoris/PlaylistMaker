package com.practicum.playlistmaker.creator

import android.app.Application
import com.practicum.playlistmaker.player.data.impl.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.practicum.playlistmaker.search.data.impl.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.impl.SearchHistory
import com.practicum.playlistmaker.player.domain.MediaPlayerManagerInteractor
import com.practicum.playlistmaker.player.domain.impl.MediaPlayerManagerInteractorImpl
import com.practicum.playlistmaker.search.domain.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.TrackInteractor
import com.practicum.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.practicum.playlistmaker.player.domain.MediaPlayerRepository
import com.practicum.playlistmaker.search.domain.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {

    private lateinit var application: Application
    fun setApplication(appl: Application) {
        application = appl
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(provideSettingsRepository())
    }
    private fun provideSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(application)
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(provideExternalNavigator())
    }
    private fun provideExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(application)
    }

    fun provideMediaPlayerManagerInteractor(): MediaPlayerManagerInteractor {
        return MediaPlayerManagerInteractorImpl(provideMediaPlayerManager())
    }
    private fun provideMediaPlayerManager(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(provideSearchHistoryRepository())
    }
    private fun provideSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(provideSearchHistory())
    }
    private fun provideSearchHistory(): SearchHistory {
        return SearchHistory(application)
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(provideTracksRepository())
    }
    private fun provideTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(provideRetrofitNetworkClient())
    }
    private fun provideRetrofitNetworkClient(): NetworkClient {
        return RetrofitNetworkClient()
    }
}