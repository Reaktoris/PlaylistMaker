package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TrackRequest
import com.practicum.playlistmaker.search.data.network.ITunesSearchApiService
import com.practicum.playlistmaker.search.data.network.NetworkClient
import java.lang.Exception

class RetrofitNetworkClient(private val iTunesSearchApiService: ITunesSearchApiService) : NetworkClient {
    override fun doRequest(dto: Any): Response {
        return try {
            if (dto is TrackRequest) {
                val response =  iTunesSearchApiService.search(dto.text).execute()
                val body = response.body() ?: Response()
                body.apply { resultCode = response.code()}
            } else {
                Response().apply { resultCode = 404 }
            }
        } catch (ex: Exception) {
            Response().apply { resultCode = 400 }
        }
    }
}