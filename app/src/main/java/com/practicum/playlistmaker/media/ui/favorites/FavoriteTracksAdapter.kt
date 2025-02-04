package com.practicum.playlistmaker.media.ui.favorites
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.model.Track

class FavoriteTracksAdapter(private val clickListener: ItemClickListener) : RecyclerView.Adapter<FavoriteTracksViewHolder> () {
    var trackList: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_view, parent, false)
        return FavoriteTracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteTracksViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {clickListener.onItemClick(trackList[position])}
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    fun interface ItemClickListener {
        fun onItemClick(track: Track)
    }
}