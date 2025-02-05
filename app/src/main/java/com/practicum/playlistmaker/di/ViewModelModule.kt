package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.ui.favorites.view_model.FavoritesViewModel
import com.practicum.playlistmaker.media.ui.playlists.playlist_page.view_model.PlaylistPageViewModel
import com.practicum.playlistmaker.media.ui.playlists.create_playlist.CreatePlaylistViewModel
import com.practicum.playlistmaker.media.ui.playlists.edit_playlist.EditPlaylistViewModel
import com.practicum.playlistmaker.media.ui.playlists.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        FavoritesViewModel(get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

     viewModel {
         SearchViewModel(get(), get())
     }

    viewModel {
        PlayerViewModel(get(), get(), get())
    }

    viewModel {
        CreatePlaylistViewModel(get(), get())
    }

    viewModel {
        PlaylistPageViewModel(get(), get())
    }

    viewModel {
        EditPlaylistViewModel(get(), get())
    }
}