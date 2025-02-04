package com.practicum.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.FavoritesInteractor
import com.practicum.playlistmaker.media.domain.PlaylistsInteractor
import com.practicum.playlistmaker.media.domain.model.Playlist
import com.practicum.playlistmaker.media.ui.playlists.PlaylistsState
import com.practicum.playlistmaker.player.domain.MediaPlayerManagerInteractor
import com.practicum.playlistmaker.player.ui.PlayerState
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val mediaPlayerManagerInteractor: MediaPlayerManagerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistsInteractor: PlaylistsInteractor
    ) : ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerState>()
    private val isFavoriteLiveData = MutableLiveData<Boolean>()
    private val isTrackAlreadyAddedLiveData = MutableLiveData<String>()
    private val playlistsStateLiveData = MutableLiveData<PlaylistsState>()

    private var timerJob: Job? = null

    fun getPlayerStateLiveData(): LiveData<PlayerState> {
        return playerStateLiveData
    }

    fun getPlaylistsStateLiveData(): LiveData<PlaylistsState> {
        return playlistsStateLiveData
    }

    fun getIsFavoriteLiveData(): LiveData<Boolean> {
        return isFavoriteLiveData
    }

    fun getIsTrackAlreadyAddedLiveData(): LiveData<String> {
        return isTrackAlreadyAddedLiveData
    }

    fun preparePlayer(url: String) {
        mediaPlayerManagerInteractor.preparePlayer(url)
        mediaPlayerManagerInteractor.setOnCompletionListener {
            playerStateLiveData.value = PlayerState.Prepared()
        }
        playerStateLiveData.value = PlayerState.Prepared()
    }

    private fun updateProgress() {
        when(playerStateLiveData.value) {
            is PlayerState.Playing -> {
                timerJob = viewModelScope.launch {
                    playerStateLiveData.value = PlayerState.Playing(getProgress())
                    delay(PROGRESS_DELAY)
                    updateProgress()
                }
            }
            is PlayerState.Paused, is PlayerState.Prepared -> {
                timerJob?.cancel()
            }
            else -> {}


        }
    }

    fun playbackControl() {
        when(playerStateLiveData.value) {
            is PlayerState.Playing -> {
                pausePlayer()
            }
            is PlayerState.Paused, is PlayerState.Prepared -> {
                startPlayer()
            }

            else -> {}
        }
    }

    private fun getProgress(): String {
        return SimpleDateFormat("mm:ss",
            Locale.getDefault()).format(mediaPlayerManagerInteractor.getCurrentPosition())
    }

    private fun startPlayer() {
        mediaPlayerManagerInteractor.startPlayer()
        playerStateLiveData.value = PlayerState.Playing(getProgress())
        updateProgress()
    }

    fun pausePlayer() {
        mediaPlayerManagerInteractor.pausePlayer()
        playerStateLiveData.value = PlayerState.Paused(getProgress())
    }

    fun release() {
        mediaPlayerManagerInteractor.release()
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            if (isFavoriteLiveData.value == true) {
                favoritesInteractor.deleteFavoriteTrack(track.trackId)
                isFavoriteLiveData.value = false
            } else {
                favoritesInteractor.addFavoriteTrack(track)
                isFavoriteLiveData.value = true
            }
        }
    }
    fun isFavoriteCheck(track: Track) {
        viewModelScope.launch {
            isFavoriteLiveData.value = favoritesInteractor.isFavoriteCheck(track.trackId)
        }
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

    fun addTrackToPlaylist(playlist: Playlist, track: Track) {
            if (playlist.tracks.contains(track.trackId.toString())) {
                    isTrackAlreadyAddedLiveData.value = "Трек уже добавлен в плейлист ${playlist.title}"
            } else {
                viewModelScope.launch {
                    playlistsInteractor.addTrackToPlaylist(playlist, track)
                    isTrackAlreadyAddedLiveData.value = "Добавлено в плейлист ${playlist.title}"
                    getPlaylists()
                }
            }

    }

    companion object {
        private const val PROGRESS_DELAY = 300L
    }

}

