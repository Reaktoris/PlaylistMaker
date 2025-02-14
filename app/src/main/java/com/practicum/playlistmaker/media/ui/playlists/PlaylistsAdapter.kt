package com.practicum.playlistmaker.media.ui.playlists
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.domain.model.Playlist

class PlaylistsAdapter(private val clickListener: ItemClickListener) : RecyclerView.Adapter<PlaylistsViewHolder> () {
    var playlists: MutableList<Playlist> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_view, parent, false)
        return PlaylistsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {clickListener.onItemClick(playlists[position])}
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    fun interface ItemClickListener {
        fun onItemClick(playlist: Playlist)
    }
}