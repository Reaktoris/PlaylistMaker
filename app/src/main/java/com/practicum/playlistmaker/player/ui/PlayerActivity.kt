package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private lateinit var viewModel: PlayerViewModel

    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[PlayerViewModel::class.java]

        val extras = intent.extras
        if (extras != null) {
            track = Gson().fromJson(extras.getString("track"), Track::class.java)
        }

        binding.songTitle.text = track.trackName
        binding.artist.text = track.artistName
        binding.time2.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.album2.text = track.collectionName
        binding.year2.text = track.releaseDate.dropLast(16)
        binding.genre2.text = track.primaryGenreName
        binding.country2.text = track.country
        binding.group1.isVisible = track.collectionName.isNotEmpty()

        Glide.with(this)
            .load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .centerCrop()
            .transform(RoundedCorners((8f * resources.displayMetrics.density).toInt()))
            .placeholder(R.drawable.album)
            .into(binding.cover)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.getPlayerStateLiveData().observe(this) {playerState ->
            when(playerState) {
                is PlayerState.Prepared -> {
                    binding.progress.text = playerState.progress
                    binding.playButton.setBackgroundResource(R.drawable.play_icon)
                }
                is PlayerState.Playing -> {
                    binding.progress.text = playerState.progress
                    binding.playButton.setBackgroundResource(R.drawable.pause_icon)
                }
                is PlayerState.Paused -> {
                    binding.progress.text = playerState.progress
                    binding.playButton.setBackgroundResource(R.drawable.play_icon)
                }
            }
        }

        preparePlayer()
    }

    private fun preparePlayer() {
        viewModel.preparePlayer(track.previewUrl)
        binding.playButton.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }
}