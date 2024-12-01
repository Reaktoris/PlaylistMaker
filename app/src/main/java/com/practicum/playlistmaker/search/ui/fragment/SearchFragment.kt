package com.practicum.playlistmaker.search.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.SearchState
import com.practicum.playlistmaker.search.ui.TRACK
import com.practicum.playlistmaker.search.ui.TracksAdapter
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModel<SearchViewModel>()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var searchRunnable: Runnable
    private lateinit var gson: Gson
    private val tracksAdapter: TracksAdapter by lazy {TracksAdapter{clickHandler(it)}}
    private val searchHistoryAdapter: TracksAdapter by lazy {TracksAdapter{clickHandler(it)}}

    private var isClickAllowed = true
    private var savedText = SEARCH_TEXT_DEF
    private val trackList = mutableListOf<Track>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchRunnable = Runnable{viewModel.searchTracks(savedText)}

        gson = Gson()

        tracksAdapter.trackList = trackList
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = tracksAdapter

        searchHistoryAdapter.trackList = viewModel.getTrackList()
        binding.searchHistoryRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.searchHistoryRecycler.adapter = searchHistoryAdapter

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
            binding.searchHistory.isVisible = false
        }

        binding.closeButton.setOnClickListener {
            binding.editText.setText(SEARCH_TEXT_DEF)
            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
            viewModel.updateTrackList()
        }

        binding.refreshButton.setOnClickListener {
            viewModel.searchTracks(savedText)
        }

        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            binding.searchHistory.isVisible = (hasFocus
                    && binding.editText.text.isEmpty()
                    && searchHistoryAdapter.trackList.isNotEmpty())
        }

        binding.editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchTracks(savedText)
            }
            false
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                savedText = s.toString()
                binding.closeButton.isVisible = !s.isNullOrEmpty()
                searchDebounce()
                if (savedText.isEmpty()) {
                    viewModel.updateTrackList()
                    handler.removeCallbacks(searchRunnable)
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        }

        binding.editText.addTextChangedListener(simpleTextWatcher)

        viewModel.getSearchStateLiveData().observe(viewLifecycleOwner) {searchState ->
            when(searchState) {
                is SearchState.Loading -> {
                    binding.foundNothingPlaceholder.isVisible = false
                    binding.internetErrorPlaceholder.isVisible = false
                    binding.progressBar.isVisible = true
                    binding.searchHistory.isVisible = false
                    binding.recyclerView.isVisible = false
                }
                is SearchState.SearchContent -> {
                    binding.foundNothingPlaceholder.isVisible = false
                    binding.internetErrorPlaceholder.isVisible = false
                    binding.progressBar.isVisible = false
                    binding.searchHistory.isVisible = false
                    binding.recyclerView.isVisible = true
                    tracksAdapter.trackList = searchState.tracks
                    tracksAdapter.notifyDataSetChanged()
                }
                is SearchState.HistoryContent -> {
                    if (binding.editText.hasFocus()
                        && binding.editText.text.isEmpty()
                        && searchHistoryAdapter.trackList.isNotEmpty()) {
                        binding.foundNothingPlaceholder.isVisible = false
                        binding.internetErrorPlaceholder.isVisible = false
                        binding.progressBar.isVisible = false
                        binding.searchHistory.isVisible = true
                        binding.recyclerView.isVisible = false
                        searchHistoryAdapter.trackList = searchState.tracks
                        searchHistoryAdapter.notifyDataSetChanged()
                    }
                }
                is SearchState.EmptyResponseError -> {
                    binding.foundNothingPlaceholder.isVisible = true
                    binding.internetErrorPlaceholder.isVisible = false
                    binding.progressBar.isVisible = false
                    binding.searchHistory.isVisible = false
                    binding.recyclerView.isVisible = false
                }
                is SearchState.ConnectionError -> {
                    binding.foundNothingPlaceholder.isVisible = false
                    binding.internetErrorPlaceholder.isVisible = true
                    binding.progressBar.isVisible = false
                    binding.searchHistory.isVisible = false
                    binding.recyclerView.isVisible = false
                }
            }
        }

    }

    private fun clickHandler(track: Track) {
        if (clickDebounce()) {
            val intent = Intent(requireContext(), PlayerActivity::class.java)
            intent.putExtra(TRACK, gson.toJson(track))
            this.startActivity(intent)
            viewModel.saveTrack(track)
            searchHistoryAdapter.trackList = viewModel.getTrackList()
            searchHistoryAdapter.notifyDataSetChanged()
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    companion object {
        const val SEARCH_TEXT_DEF =""
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}