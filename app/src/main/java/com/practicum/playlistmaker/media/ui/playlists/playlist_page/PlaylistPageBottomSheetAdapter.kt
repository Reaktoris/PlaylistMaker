package com.practicum.playlistmaker.media.ui.playlists.playlist_page
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.model.Track

class PlaylistPageBottomSheetAdapter(
    private val onItemClick: (track: Track) -> Unit,
    private val onItemLongClick: (track: Track) -> Boolean) : RecyclerView.Adapter<PlaylistPageBottomSheetViewHolder> () {
    var trackList: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistPageBottomSheetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_view, parent, false)
        return PlaylistPageBottomSheetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistPageBottomSheetViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {onItemClick(trackList[position])}
        holder.itemView.setOnLongClickListener {onItemLongClick(trackList[position])}
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

}