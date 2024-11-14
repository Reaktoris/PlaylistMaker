package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.dto.TrackRequest
import com.practicum.playlistmaker.data.repository.NetworkClient
import java.lang.Exception

class RetrofitNetworkClient : NetworkClient {
    override fun doRequest(dto: Any): Response {
        return try {
            if (dto is TrackRequest) {
                val response =  RetrofitClient.api.search(dto.text).execute()
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