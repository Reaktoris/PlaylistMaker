package com.practicum.playlistmaker.domain.manager

interface MediaPlayerManager {
    fun preparePlayer(url: String,
                      prepareListener: () -> Unit,
                      completeListener: () -> Unit)

    fun startPlayer()

    fun pausePlayer()

    fun getCurrentPosition(): Int

    fun release()
}