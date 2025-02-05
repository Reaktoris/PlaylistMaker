package com.practicum.playlistmaker.media.ui.playlists.playlist_page.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.PlaylistsInteractor
import com.practicum.playlistmaker.media.domain.model.Playlist
import com.practicum.playlistmaker.media.ui.playlists.playlist_page.TrackListState
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import kotlinx.coroutines.launch

class PlaylistPageViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val sharingInteractor: SharingInteractor) : ViewModel() {

    private val playlistLiveData = MutableLiveData<Playlist>()
    private val trackListStateLiveData = MutableLiveData<TrackListState>()
    private lateinit var savedPlaylist: Playlist
    private lateinit var savedTracks: List<Track>
    fun getPlaylistLiveData(): LiveData<Playlist> = playlistLiveData
    fun getTrackListStateLiveData(): LiveData<TrackListState> = trackListStateLiveData

    fun getPlaylistById(id: Int) {
        var playlist: Playlist
        viewModelScope.launch {
            playlist = playlistsInteractor.getPlaylistById(id)
            getTrackList(playlist.tracks)
            playlistLiveData.value = playlist
            savedPlaylist = playlist
        }
    }

    fun getTrackList(tracks: List<Int>) {
        viewModelScope.launch {
            playlistsInteractor.getTracksFromPlaylist(tracks).collect() { data ->
                savedTracks = data
                trackListStateLiveData.value = if (data.isNotEmpty()) {
                    TrackListState.Content(data)
                } else {
                    TrackListState.Empty
                }
            }
        }
    }

    fun removeTrack(track: Track) {
        viewModelScope.launch {
            playlistsInteractor.removeTrackFromPlaylist(savedPlaylist, track)
            getPlaylistById(savedPlaylist.id!!)
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            playlistsInteractor.deletePlaylist(savedPlaylist)
        }
    }

    fun sharePlaylist() {
        sharingInteractor.sharePlaylist(formatPlaylist())
    }

    private fun formatPlaylist(): String {
        val builder = StringBuilder()
        builder.append(savedPlaylist.title).append("\n")
        builder.append(savedPlaylist.description).append("\n")
        builder.append("${savedPlaylist.tracks.size} треков").append("\n")
        savedTracks.forEachIndexed() { index, track ->
            val minutes = track.trackTimeMillis / 60000
            val seconds = (track.trackTimeMillis % 60000) / 1000
            builder.append("${index + 1}. ${track.artistName} - ${track.trackName} (${minutes}:${if (seconds < 10) "0" else ""}$seconds)")
                .append("\n")
        }
        return builder.toString()
    }

    fun getMinuteWord(minutes: Int): String {
        val lastDigit = minutes % 10
        val lastTwoDigits = minutes % 100
        return when {
            lastTwoDigits in 11..19 -> WORD_1
            lastDigit == 1 -> WORD_2
            lastDigit in 2..4 -> WORD_3
            else -> WORD_1
        }
    }

    fun getTrackWord(count: Int): String {
        return when {
            count % 100 in 11..19 -> TRACK_WORD_1
            count % 10 == 1 -> TRACK_WORD_2
            count % 10 in 2..4 -> TRACK_WORD_3
            else -> TRACK_WORD_1
        }
    }
    companion object {
        const val WORD_1 = " минут"
        const val WORD_2 = " минута"
        const val WORD_3 = " минуты"
        const val TRACK_WORD_1 = " треков"
        const val TRACK_WORD_2 = " трек"
        const val TRACK_WORD_3 = " трека"
    }
}