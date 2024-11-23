package com.practicum.playlistmaker.domain.interactor.mediaPlayer

import com.practicum.playlistmaker.domain.manager.MediaPlayerManager

class MediaPlayerManagerInteractorImpl(private val mediaPlayerManager: MediaPlayerManager) : MediaPlayerManagerInteractor {

    override fun preparePlayer(url: String,
                               prepareListener: () -> Unit,
                               completeListener: () -> Unit) {
        mediaPlayerManager.preparePlayer(url, prepareListener, completeListener)
    }

    override fun startPlayer() {
        mediaPlayerManager.startPlayer()
    }

    override fun pausePlayer() {
        mediaPlayerManager.pausePlayer()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayerManager.getCurrentPosition()
    }

    override fun release() {
        mediaPlayerManager.release()
    }

}