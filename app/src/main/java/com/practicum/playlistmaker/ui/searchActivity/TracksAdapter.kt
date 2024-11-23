package com.practicum.playlistmaker.ui.searchActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.model.Track

const val TRACK = "track"

class TracksAdapter(private val clickListener: ItemClickListener) : RecyclerView.Adapter<TracksViewHolder> () {
    var trackList: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_view, parent, false)
        return TracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
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