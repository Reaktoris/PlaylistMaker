package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.data.impl.FavoritesRepositoryImpl
import com.practicum.playlistmaker.media.data.impl.PlaylistsRepositoryImpl
import com.practicum.playlistmaker.media.data.impl.StorageRepositoryImpl
import com.practicum.playlistmaker.media.domain.FavoritesRepository
import com.practicum.playlistmaker.media.domain.PlaylistsRepository
import com.practicum.playlistmaker.media.domain.StorageRepository
import com.practicum.playlistmaker.player.data.impl.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.MediaPlayerRepository
import com.practicum.playlistmaker.search.data.impl.SearchHistory
import com.practicum.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }

    single {
        SearchHistory(get(),get())
    }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get())
    }

    single<StorageRepository> {
        StorageRepositoryImpl(get(), get())
    }

    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(get())
    }
}