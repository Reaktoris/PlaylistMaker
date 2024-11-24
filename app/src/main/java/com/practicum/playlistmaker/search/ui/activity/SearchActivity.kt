package com.practicum.playlistmaker.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.ui.SearchState
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private lateinit var viewModel: SearchViewModel

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var searchRunnable: Runnable

    private lateinit var gson: Gson

    private var isClickAllowed = true
    private var savedText = SEARCH_TEXT_DEF
    private val trackList = mutableListOf<Track>()

    private lateinit var tracksAdapter: TracksAdapter
    private lateinit var searchHistoryAdapter: TracksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        searchRunnable = Runnable{viewModel.searchTracks(savedText)}

        gson = Gson()

        tracksAdapter = TracksAdapter{clickHandler(it)}
        tracksAdapter.trackList = trackList
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = tracksAdapter

        searchHistoryAdapter = TracksAdapter{clickHandler(it)}
        searchHistoryAdapter.trackList = viewModel.getTrackList()
        binding.searchHistoryRecycler.layoutManager = LinearLayoutManager(this)
        binding.searchHistoryRecycler.adapter = searchHistoryAdapter

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
            binding.searchHistory.isVisible = false
        }

        binding.closeButton.setOnClickListener {
            binding.editText.setText(SEARCH_TEXT_DEF)
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
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

        viewModel.getSearchStateLiveData().observe(this) {searchState ->
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
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(TRACK, gson.toJson(track))
            this@SearchActivity.startActivity(intent)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, savedText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedText = savedInstanceState.getString(SEARCH_TEXT, SEARCH_TEXT_DEF)
        binding.editText.setText(savedText)
    }


    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_TEXT_DEF =""
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}