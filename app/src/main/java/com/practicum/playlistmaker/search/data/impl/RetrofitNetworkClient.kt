package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TrackRequest
import com.practicum.playlistmaker.search.data.network.ITunesSearchApiService
import com.practicum.playlistmaker.search.data.network.NetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val iTunesSearchApiService: ITunesSearchApiService) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        return withContext(Dispatchers.IO){
            try {
                if (dto is TrackRequest) {
                    val response =  iTunesSearchApiService.search(dto.text)
                    response.apply { resultCode = 200}
                } else {
                    Response().apply { resultCode = 404 }
                }
            } catch (e: Throwable) {
                Response().apply { resultCode = 400 }
            }
        }
    }
}