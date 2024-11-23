package com.practicum.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.data.MediaPlayerManager

class MediaPlayerManagerImpl : MediaPlayerManager {
    private val mediaPlayer = MediaPlayer()
    override fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
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

    override fun setOnCompletionListener(onCompletion: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            onCompletion()
        }
    }
}