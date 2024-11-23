package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.domain.model.Track

data class TrackSearchResponse(val results: List<Track>) : Response()
