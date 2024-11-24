package com.practicum.playlistmaker.player.ui

sealed class PlayerState {
    data class Playing(
        val progress: String
    ) : PlayerState()

    data class Paused(
        val progress: String
    ) : PlayerState()

    data class Prepared(
        val progress: String = PROGRESS_DEFAULT
    ) : PlayerState()

    companion object {
        private const val PROGRESS_DEFAULT = "00:00"
    }
}

