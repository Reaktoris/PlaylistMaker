package com.practicum.playlistmaker.player.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.domain.model.Playlist

class PlaylistSheetViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val title: TextView = itemView.findViewById(R.id.title)
    private val count: TextView = itemView.findViewById(R.id.count)
    private val cover: ImageView = itemView.findViewById(R.id.cover)

    fun bind(item: Playlist) {
        Glide.with(itemView)
            .load(item.fileUri)
            .transform(
                CenterCrop(),
                RoundedCorners(8))
            .placeholder(R.drawable.placeholder)
            .into(cover)
        title.text = item.title
        count.text = "${item.count} ${getTrackWord(item.count)}"
    }
    private fun getTrackWord(count: Int): String {
        return when {
            count % 100 in 11..19 -> TRACK_WORD_1
            count % 10 == 1 -> TRACK_WORD_2
            count % 10 in 2..4 -> TRACK_WORD_3
            else -> TRACK_WORD_1
        }
    }

    companion object {
        const val TRACK_WORD_1 = "треков"
        const val TRACK_WORD_2 = "трек"
        const val TRACK_WORD_3 = "трека"
    }
}