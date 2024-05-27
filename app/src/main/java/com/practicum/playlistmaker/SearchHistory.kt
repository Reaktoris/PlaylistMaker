package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences) {
    private val searchHistoryList = mutableListOf<Track>()
    fun saveTrack(track: Track) {
        if (searchHistoryList.contains(track)) searchHistoryList.remove(track)
        if (searchHistoryList.size == 10) searchHistoryList.removeLast()
        searchHistoryList.add(0, track)
        sharedPreferences.edit().putString(SEARCHED_TRACKS_KEY, Gson().toJson(searchHistoryList)).apply()
    }

    fun getTrackList(): MutableList<Track>{
        val json = sharedPreferences.getString(SEARCHED_TRACKS_KEY, null) ?: return mutableListOf()
        val trackArray =  Gson().fromJson(json, Array<Track>::class.java)
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