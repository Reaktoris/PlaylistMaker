package com.practicum.playlistmaker.media.ui.playlists.edit_playlist

import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.PlaylistsInteractor
import com.practicum.playlistmaker.media.domain.StorageInteractor
import com.practicum.playlistmaker.media.domain.model.Playlist
import com.practicum.playlistmaker.media.ui.playlists.create_playlist.CreatePlaylistViewModel
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val storageInteractor: StorageInteractor,
    private val playlistsInteractor: PlaylistsInteractor,
) : CreatePlaylistViewModel(storageInteractor, playlistsInteractor) {

    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistsInteractor.updatePlaylist(playlist)
        }
    }


}