package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.MediaPlayerRepository
import com.practicum.playlistmaker.player.domain.MediaPlayerManagerInteractor

class MediaPlayerManagerInteractorImpl(private val mediaPlayerRepository: MediaPlayerRepository) :
    MediaPlayerManagerInteractor {

    override fun preparePlayer(url: String) {
        mediaPlayerRepository.preparePlayer(url)
    }

    override fun startPlayer() {
        mediaPlayerRepository.startPlayer()
    }

    override fun pausePlayer() {
        mediaPlayerRepository.pausePlayer()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayerRepository.getCurrentPosition()
    }

    override fun release() {
        mediaPlayerRepository.release()
    }

    override fun setOnCompletionListener(onCompletion: () -> Unit) {
        mediaPlayerRepository.setOnCompletionListener {
            onCompletion()
        }
    }

}