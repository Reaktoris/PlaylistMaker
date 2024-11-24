package com.practicum.playlistmaker.player.domain

interface MediaPlayerManagerInteractor {

    fun preparePlayer(url: String)

    fun startPlayer()

    fun pausePlayer()

    fun getCurrentPosition(): Int

    fun release()

    fun setOnCompletionListener(onCompletion: () -> Unit)
}