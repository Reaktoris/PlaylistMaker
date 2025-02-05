package com.practicum.playlistmaker.media.ui.playlists.playlist_page

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistPageBottomSheetViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

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
        itemTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
        itemArtistName.text = ""
        itemArtistName.text = item.artistName
    }

}