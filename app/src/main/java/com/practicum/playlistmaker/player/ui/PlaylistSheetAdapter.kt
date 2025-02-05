package com.practicum.playlistmaker.player.ui
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.domain.model.Playlist

class PlaylistSheetAdapter(private var playlists: List<Playlist>,
                           var onItemClick : ((Playlist) -> Unit)? = null) : RecyclerView.Adapter<PlaylistSheetViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSheetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlists_view_small, parent, false)
        return PlaylistSheetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistSheetViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(playlists[position])
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    fun updatePlaylists(newPlaylists: List<Playlist>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }
}