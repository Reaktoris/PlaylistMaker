package com.practicum.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.player.ui.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel : ViewModel() {
    private val mediaPlayerManagerInteractor = Creator.provideMediaPlayerManagerInteractor()
    private val handler = Handler(Looper.getMainLooper())

    private val playerStateLiveData = MutableLiveData<PlayerState>()

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
        handler.post(
            object : Runnable{
                override fun run() {
                    when(playerStateLiveData.value) {
                        is PlayerState.Playing -> {
                            playerStateLiveData.value = PlayerState.Playing(getProgress())
                            handler.postDelayed(this, PROGRESS_DELAY)
                        }
                        is PlayerState.Paused, is PlayerState.Prepared -> {
                            handler.removeCallbacks(this)
                        }
                        else -> {}
                    }
                }
            }
        )
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