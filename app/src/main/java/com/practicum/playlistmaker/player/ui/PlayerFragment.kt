package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.media.domain.model.Playlist
import com.practicum.playlistmaker.media.ui.playlists.PlaylistsState
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.TRACK
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment() {
    private lateinit var binding: FragmentPlayerBinding
    private val viewModel by viewModel<PlayerViewModel>()
    private lateinit var track: Track
    private var playListAdapter: PlaylistSheetAdapter? = null
    private val playlists = ArrayList<Playlist>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        track = Gson().fromJson(arguments?.getString(TRACK), Track::class.java)


        playListAdapter = PlaylistSheetAdapter(playlists)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = playListAdapter

        val overlay = binding.overlay
        val bottomSheetContainer = binding.playlistsBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.isVisible = false
                    }
                    else -> {
                        overlay.isVisible = true
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

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
            findNavController().navigateUp()
        }
        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.likeButton.setOnClickListener {
            viewModel.onFavoriteClicked(track)
        }

        binding.addButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.createPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_createPlaylistFragment)
        }

        viewModel.getPlayerStateLiveData().observe(viewLifecycleOwner) {playerState ->
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

        viewModel.getIsFavoriteLiveData().observe(viewLifecycleOwner) {
            if (it) {
                binding.likeButton.setBackgroundResource(R.drawable.like_icon_1)
                track.isFavorite = true
            } else {
                binding.likeButton.setBackgroundResource(R.drawable.like_icon)
                track.isFavorite = false
            }
        }

        viewModel.getPlaylistsStateLiveData().observe(viewLifecycleOwner) {state ->
            when(state) {
                is PlaylistsState.Content -> {
                    playlists.clear()
                    playlists.addAll(state.playlists)
                    playListAdapter?.updatePlaylists(state.playlists)
                    playListAdapter?.notifyDataSetChanged()
                } else -> {}
            }
        }

        viewModel.getIsTrackAlreadyAddedLiveData().observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(),it, Toast.LENGTH_SHORT).show()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        playListAdapter?.onItemClick = {
            viewModel.addTrackToPlaylist(it, track)
        }

        preparePlayer()
        viewModel.getPlaylists()
    }

    private fun preparePlayer() {
        viewModel.preparePlayer(track.previewUrl)
        viewModel.isFavoriteCheck(track)
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