package com.practicum.playlistmaker

import android.view.RoundedCorner
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class ItemsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val itemTrackName: TextView = itemView.findViewById(R.id.item_top_text)
    private val itemTrackTime: TextView = itemView.findViewById(R.id.item_time_text)
    private val itemArtistName: TextView = itemView.findViewById(R.id.item_bottom_text)
    private val itemTrackCover: ImageView = itemView.findViewById(R.id.item_image)

    fun bind(item: Track) {
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.placeholder)
            .into(itemTrackCover)
        itemTrackName.text = item.trackName
        itemTrackTime.text = item.trackTime
        itemArtistName.text = item.artistName
    }
}