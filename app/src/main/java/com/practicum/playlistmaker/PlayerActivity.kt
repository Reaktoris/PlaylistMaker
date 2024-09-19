package com.practicum.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import kotlinx.coroutines.Runnable
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val PROGRESS_DELAY = 500L
    }
    private lateinit var track: Track
    private lateinit var playButton: ImageButton
    private lateinit var progress: TextView
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val extras = intent.extras
        if (extras != null) {
            track = Gson().fromJson(extras.getString("track"), Track::class.java)
        }

        playButton = findViewById(R.id.play_button)
        progress = findViewById(R.id.progress)
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
            .transform(RoundedCorners((8f * resources.displayMetrics.density).toInt()))
            .placeholder(R.drawable.album)
            .into(cover)

        albumGroup.isVisible = track.collectionName.isNotEmpty()

        toolbar.setNavigationOnClickListener { finish() }
        playButton.setOnClickListener { playbackControl() }

        preparePlayer()

    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        playerState = STATE_DEFAULT
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setBackgroundResource(R.drawable.play_icon)
            playerState = STATE_PREPARED
            progress.text = "00:00"
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setBackgroundResource(R.drawable.pause_icon)
        playerState = STATE_PLAYING
        updateProgress()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setBackgroundResource(R.drawable.play_icon)
        playerState = STATE_PAUSED
    }

    private fun updateProgress() {
        handler.post(
            object : Runnable{
                override fun run() {
                    when(playerState) {
                        STATE_PLAYING -> {
                            progress.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                            handler.postDelayed(this, PROGRESS_DELAY)
                        }
                        STATE_PREPARED, STATE_PAUSED, STATE_DEFAULT -> {
                            handler.removeCallbacks(this)
                        }
                    }
                }
            }
        )
    }
}