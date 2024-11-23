package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.data.MediaPlayerManager
import com.practicum.playlistmaker.player.domain.MediaPlayerManagerInteractor

class MediaPlayerManagerInteractorImpl(private val mediaPlayerManager: MediaPlayerManager) :
    MediaPlayerManagerInteractor {

    override fun preparePlayer(url: String) {
        mediaPlayerManager.preparePlayer(url)
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

    override fun setOnCompletionListener(onCompletion: () -> Unit) {
        mediaPlayerManager.setOnCompletionListener {
            onCompletion()
        }
    }

}