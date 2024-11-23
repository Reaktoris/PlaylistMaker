package com.practicum.playlistmaker.player.data

interface MediaPlayerManager {
    fun preparePlayer(url: String)

    fun startPlayer()

    fun pausePlayer()

    fun getCurrentPosition(): Int

    fun release()

    fun setOnCompletionListener(onCompletion: () -> Unit)
}