package com.practicum.playlistmaker.creator

import android.app.Application
import com.practicum.playlistmaker.data.manager.MediaPlayerManagerImpl
import com.practicum.playlistmaker.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.NetworkClient
import com.practicum.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.sharedPreferences.SearchHistory
import com.practicum.playlistmaker.domain.interactor.mediaPlayer.MediaPlayerManagerInteractor
import com.practicum.playlistmaker.domain.interactor.mediaPlayer.MediaPlayerManagerInteractorImpl
import com.practicum.playlistmaker.domain.interactor.searchHistory.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.interactor.searchHistory.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.domain.interactor.track.TrackInteractor
import com.practicum.playlistmaker.domain.interactor.track.TrackInteractorImpl
import com.practicum.playlistmaker.domain.manager.MediaPlayerManager
import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository
import com.practicum.playlistmaker.domain.repository.TracksRepository

object Creator {
    //MediaPlayerManagerInteractor
    fun provideMediaPlayerManagerInteractor(): MediaPlayerManagerInteractor {
        return MediaPlayerManagerInteractorImpl(provideMediaPlayerManager())
    }

    private fun provideMediaPlayerManager(): MediaPlayerManager {
        return MediaPlayerManagerImpl()
    }

    //Application
    private lateinit var application: Application

    fun getApplication(appl: Application) {
        application = appl
    }

    //SearchHistoryInteractor
    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(provideSearchHistoryRepository())
    }

    private fun provideSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(provideSearchHistory())
    }

    private fun provideSearchHistory(): SearchHistory {
        return SearchHistory(application)
    }

    //TrackInteractor
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