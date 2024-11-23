package com.practicum.playlistmaker.sharing.domain.impl

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val context: Context
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
        return context.getString(R.string.practicum_url)
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.eula_url)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            email = context.getString(R.string.support_email),
            subject = context.getString(R.string.email_subject),
            text = context.getString(R.string.email_body)
        )
    }
}