package com.practicum.playlistmaker.media.data.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.media.data.db.AppDatabase
import com.practicum.playlistmaker.media.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.media.data.db.entity.PlaylistTrackEntity
import com.practicum.playlistmaker.media.domain.PlaylistsRepository
import com.practicum.playlistmaker.media.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(private val appDatabase: AppDatabase) : PlaylistsRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.getPlaylistDao().insertPlaylist(convertFromPlaylist(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow{
        emit(convertFromPlaylistEntities(appDatabase.getPlaylistDao().getPlaylists()))
    }.map{it.reversed()}

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        playlist.tracks.add(track.trackId)
        playlist.count++
        appDatabase.getPlaylistDao().updatePlaylist(convertFromPlaylist(playlist))
        appDatabase.getPlaylistTrackDao().insertPlaylistTrack(convertFromTrack(track))
    }

    override suspend fun getPlaylistById(id: Int): Playlist {
        return convertFromPlaylistEntitie(appDatabase.getPlaylistDao().getPlaylistById(id))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        val trackList = playlist.tracks
        if (playlist.id != null) {
            appDatabase.getPlaylistDao().deletePlaylistById(playlist.id)
            trackList.forEach { trackId -> removePlaylistTrack(trackId) }
        }
    }

    override suspend fun removeTrackFromPlaylist(playlist: Playlist, track: Track) {
        playlist.tracks.remove(track.trackId)
        playlist.count--
        appDatabase.getPlaylistDao().updatePlaylist(convertFromPlaylist(playlist))
        removePlaylistTrack(track.trackId)
    }

    override suspend fun removePlaylistTrack(trackId: Int) {
        getPlaylists().collect { playlists ->
            val isTrackExist = playlists.any { playlist ->
                playlist.tracks.any { track -> track == trackId }
            }
            if (!isTrackExist) {
                appDatabase.getPlaylistTrackDao().deletePlaylistTrack(trackId)
            }
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.getPlaylistDao().updatePlaylist(convertFromPlaylist(playlist))
    }

    override fun getTracksFromPlaylist(tracks: List<Int>): Flow<List<Track>> = flow {
        val allTracks = convertFromTrackEntities(appDatabase.getPlaylistTrackDao().getTracks())
        val filteredTracks = allTracks.filter { it.trackId in tracks }
        emit(filteredTracks)
    }

    private fun convertFromPlaylistEntities(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map {
            Playlist(
                it.id,
                it.title,
                it.description,
                it.fileUri,
                Gson().fromJson(it.tracks, object : TypeToken<MutableList<Int>>() {}.type),
                it.count
            )
        }
    }
    private fun convertFromTrackEntities(tracks: List<PlaylistTrackEntity>): List<Track> {
        return tracks.map { trackEntity ->
            Track(
                trackEntity.trackName,
                trackEntity.artistName,
                trackEntity.trackTimeMillis,
                trackEntity.artworkUrl100,
                trackEntity.previewUrl,
                trackEntity.trackId,
                trackEntity.collectionName,
                trackEntity.releaseDate,
                trackEntity.primaryGenreName,
                trackEntity.country)
        }
    }

    private fun convertFromPlaylist(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.title,
            playlist.description,
            playlist.fileUri,
            Gson().toJson(playlist.tracks),
            playlist.count
        )
    }

    private fun convertFromPlaylistEntitie(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.id,
            playlist.title,
            playlist.description,
            playlist.fileUri,
            Gson().fromJson(playlist.tracks, object : TypeToken<MutableList<Int>>() {}.type),
            playlist.count
        )
    }

    private fun convertFromTrack(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(
            id = null,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.previewUrl,
            track.trackId,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country
        )
    }
}