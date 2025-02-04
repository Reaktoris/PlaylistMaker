package com.practicum.playlistmaker.media.ui.playlists.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.PlaylistsInteractor
import com.practicum.playlistmaker.media.domain.StorageInteractor
import com.practicum.playlistmaker.media.domain.model.Playlist
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val storageInteractor: StorageInteractor,
    private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {
    fun saveImage(uri: Uri): String {
        return storageInteractor.saveImage(uri)
    }

    fun loadImage(fileName: String): Uri {
        return storageInteractor.loadImage(fileName)
    }

    fun addPlaylist(title: String, description: String?, fileUri: String?) {
        viewModelScope.launch {
            playlistsInteractor.addPlaylist(
                Playlist(
                    id = null ,
                    title,
                    description,
                    fileUri,
                    mutableListOf(),
                    0
                )
            )
        }
    }


}