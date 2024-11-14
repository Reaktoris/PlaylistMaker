package com.practicum.playlistmaker.data.manager

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.manager.MediaPlayerManager

class MediaPlayerManagerImpl() : MediaPlayerManager {
    val mediaPlayer = MediaPlayer()
    override fun preparePlayer(url: String,
                               prepareListener: () -> Unit,
                               completeListener: () -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            prepareListener()
        }
        mediaPlayer.setOnCompletionListener {
            completeListener()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun release() {
        mediaPlayer.release()
    }
}