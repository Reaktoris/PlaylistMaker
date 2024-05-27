package com.practicum.playlistmaker
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ItemsAdapter : RecyclerView.Adapter<ItemsViewHolder> () {
    var trackList: MutableList<Track> = mutableListOf()
    lateinit var searchHistory: SearchHistory

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_view, parent, false)
        return ItemsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            searchHistory.saveTrack(trackList[position])
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}