package com.practicum.playlistmaker.search.data.dto

import com.practicum.playlistmaker.search.domain.model.Track

data class TrackSearchResponse(val results: List<Track>) : Response()
