package com.practicum.playlistmaker.search.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.SearchState
import com.practicum.playlistmaker.search.ui.TracksAdapter
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModel<SearchViewModel>()
    private lateinit var gson: Gson
    private val tracksAdapter: TracksAdapter by lazy {TracksAdapter{clickHandler(it)}}
    private val searchHistoryAdapter: TracksAdapter by lazy {TracksAdapter{clickHandler(it)}}

    private var isClickAllowed = true
    private var searchText = SEARCH_TEXT_DEF
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
        isClickAllowed = true
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
            viewModel.searchTracks(searchText)
        }

        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            binding.searchHistory.isVisible = (hasFocus
                    && binding.editText.text.isEmpty()
                    && searchHistoryAdapter.trackList.isNotEmpty())
        }

        binding.editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchTracks(searchText)
            }
            false
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
                binding.closeButton.isVisible = !s.isNullOrEmpty()
                viewModel.searchDebounce(searchText)
                if (searchText.isEmpty()) {
                    viewModel.updateTrackList()
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
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToPlayerFragment(gson.toJson(track)))
            viewModel.saveTrack(track)
            searchHistoryAdapter.trackList = viewModel.getTrackList()
            searchHistoryAdapter.notifyDataSetChanged()
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
        const val SEARCH_TEXT_DEF =""
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}