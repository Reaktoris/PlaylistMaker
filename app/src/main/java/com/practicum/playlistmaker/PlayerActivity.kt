package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    lateinit var track: Track
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val extras = intent.extras
        if (extras != null) {
            track = Gson().fromJson(extras.getString("track"), Track::class.java)
        }

        val albumGroup = findViewById<Group>(R.id.group1)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val cover = findViewById<ImageView>(R.id.cover)
        val trackName = findViewById<TextView>(R.id.song_title)
        val artistName = findViewById<TextView>(R.id.artist)
        val trackTimeMillis = findViewById<TextView>(R.id.time2)
        val collectionName = findViewById<TextView>(R.id.album2)
        val releaseDate = findViewById<TextView>(R.id.year2)
        val primaryGenreName = findViewById<TextView>(R.id.genre2)
        val country = findViewById<TextView>(R.id.country2)

        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTimeMillis.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        collectionName.text = track.collectionName
        releaseDate.text = track.releaseDate.dropLast(16)
        primaryGenreName.text = track.primaryGenreName
        country.text = track.country

        Glide.with(this)
            .load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .centerCrop()
            .transform(RoundedCorners(8))
            .placeholder(R.drawable.album)
            .into(cover)

        if (collectionName.text == "No Strings Attached") albumGroup.isVisible = false

        toolbar.setNavigationOnClickListener { finish() }
    }


}