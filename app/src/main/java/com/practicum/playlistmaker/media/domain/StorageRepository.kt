package com.practicum.playlistmaker.media.domain

import android.net.Uri

interface StorageRepository {
    fun saveImage(uri: Uri): String
    fun loadImage(fileName: String): Uri
}