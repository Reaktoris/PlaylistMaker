package com.practicum.playlistmaker.search.domain.model

sealed interface ConsumerData<T> {
    data class Data<T>(val value: T) : ConsumerData<T>
    class Error<T> : ConsumerData<T>
}