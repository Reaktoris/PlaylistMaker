package com.practicum.playlistmaker.media.ui.favorites.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.FavoritesInteractor
import com.practicum.playlistmaker.media.ui.favorites.FavoritesState
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoritesInteractor: FavoritesInteractor) : ViewModel() {

    private val favoritesStateLiveData = MutableLiveData<FavoritesState>()

    fun getFavoritesStateLiveData(): LiveData<FavoritesState> {
        return favoritesStateLiveData
    }

    fun getTrackList() {
        viewModelScope.launch {
            favoritesInteractor.getFavoriteTracks().collect() { data ->
                favoritesStateLiveData.value = if (data.isNotEmpty()) {
                    FavoritesState.Content(data)
                } else {
                    FavoritesState.Empty
                }
            }
        }
    }

}