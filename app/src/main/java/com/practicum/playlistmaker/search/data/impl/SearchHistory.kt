package com.practicum.playlistmaker.search.data.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.app.SEARCHED_TRACKS_KEY
import com.practicum.playlistmaker.search.data.dto.TrackDto

class SearchHistory(private val gson: Gson, private val sharedPreferences: SharedPreferences) {
    private val searchHistoryList = mutableListOf<TrackDto>()
    fun saveTrack(trackDto: TrackDto) {
        if (searchHistoryList.contains(trackDto)) searchHistoryList.remove(trackDto)
        if (searchHistoryList.size == 10) searchHistoryList.removeLast()
        searchHistoryList.add(0, trackDto)
        sharedPreferences.edit().putString(SEARCHED_TRACKS_KEY, gson.toJson(searchHistoryList)).apply()
    }

    fun getTrackList(): MutableList<TrackDto>{
        val json = sharedPreferences.getString(SEARCHED_TRACKS_KEY, null) ?: return mutableListOf()
        val trackArray =  gson.fromJson(json, Array<TrackDto>::class.java)
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