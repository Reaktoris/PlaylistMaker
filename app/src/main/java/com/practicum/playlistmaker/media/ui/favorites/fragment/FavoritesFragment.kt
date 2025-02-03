package com.practicum.playlistmaker.media.ui.favorites.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.media.ui.favorites.FavoriteTracksAdapter
import com.practicum.playlistmaker.media.ui.favorites.FavoritesState
import com.practicum.playlistmaker.media.ui.favorites.view_model.FavoritesViewModel
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.TRACK
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel by viewModel<FavoritesViewModel>()
    private lateinit var gson: Gson
    private val favoriteTracksAdapter: FavoriteTracksAdapter by lazy {FavoriteTracksAdapter{clickHandler(it)}}

    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gson = Gson()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = favoriteTracksAdapter

        viewModel.getTrackList()

        viewModel.getFavoritesStateLiveData().observe(viewLifecycleOwner) {state ->
            when(state) {
                is FavoritesState.Content -> {
                    favoriteTracksAdapter.trackList = state.tracks.toMutableList()
                    favoriteTracksAdapter.notifyDataSetChanged()
                    binding.recyclerView.isVisible = true
                    binding.placeholder.isVisible = false
                }
                is FavoritesState.Empty -> {
                    binding.recyclerView.isVisible = false
                    binding.placeholder.isVisible = true
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTrackList()
    }

    private fun clickHandler(track: Track) {
        if (clickDebounce()) {
            val intent = Intent(requireContext(), PlayerActivity::class.java)
            intent.putExtra(TRACK, gson.toJson(track))
            this.startActivity(intent)
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
        fun newInstance() = FavoritesFragment()
    }
}