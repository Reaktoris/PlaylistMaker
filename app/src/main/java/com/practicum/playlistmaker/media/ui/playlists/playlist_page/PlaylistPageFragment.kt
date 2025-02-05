package com.practicum.playlistmaker.media.ui.playlists.playlist_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistPageBinding
import com.practicum.playlistmaker.media.domain.model.Playlist
import com.practicum.playlistmaker.media.ui.playlists.playlist_page.view_model.PlaylistPageViewModel
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.fragment.SearchFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistPageFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistPageBinding
    private val viewModel by viewModel<PlaylistPageViewModel>()
    private val playlistPageBottomSheetAdapter = PlaylistPageBottomSheetAdapter(
        {click(it)},
        {longClick(it)
            true})

    private var isClickAllowed = true
    private var isTracksExist = false
    private lateinit var playlist: Playlist

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isClickAllowed = true

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = playlistPageBottomSheetAdapter

        val playlistId = arguments?.getInt("playlistId")


        viewModel.getPlaylistLiveData().observe(viewLifecycleOwner) {
            Glide.with(requireActivity())
                .load(it.fileUri)
                .centerCrop()
                .into(binding.cover)
            binding.title.text = it.title
            binding.description.text =  it.description
            binding.description.isVisible = !it.description.isNullOrEmpty()

            Glide.with(requireActivity())
                .load(it.fileUri)
                .transform(
                    CenterCrop(),
                    RoundedCorners((2f * resources.displayMetrics.density).toInt())
                )
                .placeholder(R.drawable.album)
                .into(binding.moreCover)
            binding.moreTitle.text = it.title
            playlist = it
        }

        viewModel.getTrackListStateLiveData().observe(viewLifecycleOwner) {
            when(it) {
                is TrackListState.Content -> {
                    isTracksExist = true
                    binding.timeAndCount.isVisible = true
                    playlistPageBottomSheetAdapter.trackList = it.tracks.toMutableList()
                    playlistPageBottomSheetAdapter.notifyDataSetChanged()
                    val count = it.tracks.size.toString() + viewModel.getTrackWord(it.tracks.size)
                    binding.count.text = count
                    binding.moreCount.text = count
                    var time = 0
                    it.tracks.forEach {track ->
                        time += track.trackTimeMillis
                    }
                    binding.time.text = SimpleDateFormat("m", Locale.getDefault()).format(time) + viewModel.getMinuteWord(time)

                }
                else -> {
                    playlistPageBottomSheetAdapter.trackList.clear()
                    playlistPageBottomSheetAdapter.notifyDataSetChanged()
                    isTracksExist = false
                    binding.timeAndCount.isVisible = false
                    binding.moreCount.setText(R.string.zero_tracks)
                }
            }

        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.moreBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }
                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.share.setOnClickListener {
            share()
        }

        binding.more.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.shareButton.setOnClickListener {
            share()
        }

        binding.editButton.setOnClickListener {
            findNavController().navigate(PlaylistPageFragmentDirections.actionPlaylistPageFragmentToEditPlaylistFragment(Gson().toJson(playlist)))
        }

        binding.deliteButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.delete_playlist)
                .setMessage(R.string.delete_confirm)
                .setNegativeButton(R.string.no){ dialog, _ ->
                    dialog.dismiss()
                }.setPositiveButton(R.string.yes){_, _, ->
                    viewModel.deletePlaylist()
                    findNavController().navigateUp()
                }.show()
        }

        if (playlistId != null) {
            viewModel.getPlaylistById(playlistId)
        }
    }

    private fun share() {
        if (isTracksExist) {
            viewModel.sharePlaylist()
        } else {
            Toast.makeText(requireContext(), R.string.no_tracks, Toast.LENGTH_SHORT).show()
        }
    }

    private fun click(track: Track){
        if(clickDebounce()) {
            findNavController().navigate(
                PlaylistPageFragmentDirections.actionPlaylistPageFragmentToPlayerFragment(Gson().toJson(track))
            )
        }
    }

    private fun longClick(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_track)
            .setMessage(R.string.delete_track_description)
            .setNegativeButton(R.string.cancel){ dialog, _ ->
                dialog.dismiss()
            }.setPositiveButton(R.string.delete){_, _, ->
                viewModel.removeTrack(track)
            }.show()
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(SearchFragment.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        fun newInstance() = PlaylistPageFragment()
    }

}