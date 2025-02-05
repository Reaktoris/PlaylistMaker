package com.practicum.playlistmaker.media.data.impl

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.practicum.playlistmaker.app.FILE_NAME_KEY
import com.practicum.playlistmaker.media.domain.StorageRepository
import java.io.File
import java.io.FileOutputStream

class StorageRepositoryImpl(val context: Context, private val sharedPreferences: SharedPreferences) : StorageRepository {
    override fun saveImage(uri: Uri): String {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlistMakerAlbum")
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val fileName = sharedPreferences.getInt(FILE_NAME_KEY, 1)
        sharedPreferences.edit().putInt(FILE_NAME_KEY, fileName + 1).apply()
        val file = File(filePath, fileName.toString())
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return fileName.toString()
    }

    override fun loadImage(fileName: String): Uri {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlistMakerAlbum")
        val file = File(filePath, fileName)

        return file.toUri()
    }
}