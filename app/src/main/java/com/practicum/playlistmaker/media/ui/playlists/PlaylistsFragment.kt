package com.practicum.playlistmaker.media.ui.playlists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.media.ui.playlists.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistsBinding
    private val viewModel by viewModel<PlaylistsViewModel>()
    private val playlistsAdapter = PlaylistsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = playlistsAdapter

        viewModel.getPlaylists()

        viewModel.getPlaylistsStateLiveData().observe(viewLifecycleOwner) {state ->
            when(state) {
                is PlaylistsState.Content -> {
                    playlistsAdapter.playlists = state.playlists.toMutableList()
                    playlistsAdapter.notifyDataSetChanged()
                    binding.recyclerView.isVisible = true
                    binding.placeholder.isVisible = false
                }
                is PlaylistsState.Empty -> {
                    binding.recyclerView.isVisible = false
                    binding.placeholder.isVisible = true
                }
            }
        }

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_createPlaylistFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylists()
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}