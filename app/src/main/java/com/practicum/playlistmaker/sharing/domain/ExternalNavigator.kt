package com.practicum.playlistmaker.sharing.domain

interface ExternalNavigator {
    fun shareLink()

    fun openLink()

    fun openEmail()

    fun shareText(text: String)
}