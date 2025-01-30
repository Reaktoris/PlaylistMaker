package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.ui.view_model.FavoritesViewModel
import com.practicum.playlistmaker.media.ui.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        FavoritesViewModel()
    }

    viewModel {
        PlaylistsViewModel()
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

     viewModel {
         SearchViewModel(get(), get())
     }

    viewModel {
        PlayerViewModel(get())
    }
}