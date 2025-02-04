package com.practicum.playlistmaker.media.ui.playlists.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.PlaylistsInteractor
import com.practicum.playlistmaker.media.ui.playlists.PlaylistsState
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val playlistsStateLiveData = MutableLiveData<PlaylistsState>()

    fun getPlaylistsStateLiveData(): LiveData<PlaylistsState> {
        return playlistsStateLiveData
    }

    fun getPlaylists() {
        viewModelScope.launch {
            playlistsInteractor.getPlaylists().collect() { data ->
                playlistsStateLiveData.value = if (data.isNotEmpty()) {
                    PlaylistsState.Content(data)
                } else {
                    PlaylistsState.Empty
                }
            }
        }
    }
}