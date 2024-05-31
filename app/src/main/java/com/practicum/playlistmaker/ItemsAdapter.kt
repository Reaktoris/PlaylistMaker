package com.practicum.playlistmaker
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

const val TRACK = "track"
class ItemsAdapter : RecyclerView.Adapter<ItemsViewHolder> () {
    var trackList: MutableList<Track> = mutableListOf()
    lateinit var searchHistory: SearchHistory
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_view, parent, false)
        return ItemsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(TRACK, Gson().toJson(trackList[position]))
            context.startActivity(intent)
            searchHistory.saveTrack(trackList[position])
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}