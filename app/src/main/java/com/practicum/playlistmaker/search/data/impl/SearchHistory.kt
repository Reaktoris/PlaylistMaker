package com.practicum.playlistmaker.search.data.impl

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.practicum.playlistmaker.app.PREFERENCES
import com.practicum.playlistmaker.app.SEARCHED_TRACKS_KEY
import com.practicum.playlistmaker.search.data.dto.TrackDto

class SearchHistory(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(PREFERENCES, AppCompatActivity.MODE_PRIVATE)
    private val searchHistoryList = mutableListOf<TrackDto>()
    fun saveTrack(trackDto: TrackDto) {
        if (searchHistoryList.contains(trackDto)) searchHistoryList.remove(trackDto)
        if (searchHistoryList.size == 10) searchHistoryList.removeLast()
        searchHistoryList.add(0, trackDto)
        sharedPreferences.edit().putString(SEARCHED_TRACKS_KEY, Gson().toJson(searchHistoryList)).apply()
    }

    fun getTrackList(): MutableList<TrackDto>{
        val json = sharedPreferences.getString(SEARCHED_TRACKS_KEY, null) ?: return mutableListOf()
        val trackArray =  Gson().fromJson(json, Array<TrackDto>::class.java)
        val trackList = trackArray.toMutableList()
        searchHistoryList.clear()
        searchHistoryList.addAll(trackList)
        return trackList
    }

    fun clearHistory() {
        searchHistoryList.clear()
        sharedPreferences.edit().clear().apply()
    }
}