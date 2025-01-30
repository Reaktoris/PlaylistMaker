package com.practicum.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.MediaPlayerManagerInteractor
import com.practicum.playlistmaker.player.ui.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val mediaPlayerManagerInteractor: MediaPlayerManagerInteractor
    ) : ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerState>()

    private var timerJob: Job? = null

    fun getPlayerStateLiveData(): LiveData<PlayerState> {
        return playerStateLiveData
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

    companion object {
        private const val PROGRESS_DELAY = 500L
    }

}