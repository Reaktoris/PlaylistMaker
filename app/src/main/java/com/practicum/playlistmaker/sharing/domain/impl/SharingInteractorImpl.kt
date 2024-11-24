package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return LINK_SHARE
    }

    private fun getTermsLink(): String {
        return LINK_TERMS
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            email = EMAIL,
            subject = SUBJECT,
            text = TEXT
        )
    }
    companion object {
        private const val LINK_SHARE = "https://practicum.yandex.ru/android-developer/"
        private const val LINK_TERMS = "https://yandex.ru/legal/practicum_offer/"
        private const val EMAIL = "scotty200305@gmail.com"
        private const val SUBJECT = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
        private const val TEXT = "Спасибо разработчикам и разработчицам за крутое приложение!"
    }
}