package com.practicum.playlistmaker.domain.interactor.mediaPlayer

interface MediaPlayerManagerInteractor {

    fun preparePlayer(url: String,
                      prepareListener: () -> Unit,
                      completeListener: () -> Unit)

    fun startPlayer()

    fun pausePlayer()

    fun getCurrentPosition(): Int

    fun release()

}