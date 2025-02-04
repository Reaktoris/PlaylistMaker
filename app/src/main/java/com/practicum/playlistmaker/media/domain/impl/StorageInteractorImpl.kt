package com.practicum.playlistmaker.media.domain.impl

import android.net.Uri
import com.practicum.playlistmaker.media.domain.StorageInteractor
import com.practicum.playlistmaker.media.domain.StorageRepository

class StorageInteractorImpl(private val storageRepository: StorageRepository) : StorageInteractor {
    override fun saveImage(uri: Uri): String {
        return storageRepository.saveImage(uri)
    }

    override fun loadImage(fileName: String): Uri {
        return storageRepository.loadImage(fileName)
    }
}