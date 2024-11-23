package com.practicum.playlistmaker.ui.searchActivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator

import com.practicum.playlistmaker.domain.interactor.track.TrackInteractor
import com.practicum.playlistmaker.domain.model.ConsumerData
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.ui.PlayerActivity

class SearchActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable{search()}
    private var isClickAllowed = true

    private val trackInteractor = Creator.provideTrackInteractor()
    private val searchHistoryInteractor = Creator.provideSearchHistoryInteractor()

    private var savedText = SEARCH_TEXT_DEF
    private val trackList = mutableListOf<Track>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchHistoryRecycler: RecyclerView
    private lateinit var editText: EditText
    private lateinit var tracksAdapter: TracksAdapter
    private lateinit var searchHistoryAdapter: TracksAdapter
    private lateinit var foundNothingPlaceholder: LinearLayout
    private lateinit var internetErrorPlaceholder: LinearLayout
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerView = findViewById(R.id.recyclerView)
        tracksAdapter = TracksAdapter{
            if (clickDebounce()) {
                val intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra(TRACK, Gson().toJson(it))
                this@SearchActivity.startActivity(intent)
                searchHistoryInteractor.saveTrack(it)
                searchHistoryAdapter.trackList = searchHistoryInteractor.getTrackList()
                searchHistoryAdapter.notifyDataSetChanged()
            }
        }
        tracksAdapter.trackList = trackList
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = tracksAdapter

        searchHistoryRecycler = findViewById(R.id.search_history_recycler)
        searchHistoryAdapter = TracksAdapter{
            if (clickDebounce()) {
                val intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra(TRACK, Gson().toJson(it))
                this@SearchActivity.startActivity(intent)
                searchHistoryInteractor.saveTrack(it)
                searchHistoryAdapter.trackList = searchHistoryInteractor.getTrackList()
                searchHistoryAdapter.notifyDataSetChanged()
            }
        }
        searchHistoryAdapter.trackList = searchHistoryInteractor.getTrackList()
        searchHistoryRecycler.layoutManager = LinearLayoutManager(this)
        searchHistoryRecycler.adapter = searchHistoryAdapter

        editText = findViewById(R.id.edit_text_id)
        progressBar = findViewById(R.id.progress_bar)
        val closeButton = findViewById<ImageView>(R.id.close_button)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        foundNothingPlaceholder = findViewById(R.id.found_nothing_placeholder)
        internetErrorPlaceholder = findViewById(R.id.internet_error_placeholder)
        val refreshButton = findViewById<Button>(R.id.refresh_button)
        val searchHistoryLayout = findViewById<LinearLayout>(R.id.search_history)
        val clearHistoryButton = findViewById<Button>(R.id.clear_history_button)




        clearHistoryButton.setOnClickListener {
            searchHistoryInteractor.clearHistory()
            searchHistoryAdapter.trackList = searchHistoryInteractor.getTrackList()
            searchHistoryAdapter.notifyDataSetChanged()
            searchHistoryLayout.isVisible = false
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            searchHistoryLayout.isVisible = (hasFocus
                    && editText.text.isEmpty()
                    && searchHistoryAdapter.trackList.isNotEmpty())
        }

        toolbar.setNavigationOnClickListener { finish() }

        closeButton.setOnClickListener {
            editText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
        }

        refreshButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            searchDebounce()
        }

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }



        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                savedText = s.toString()
                closeButton.isVisible = !s.isNullOrEmpty()
                searchHistoryLayout.isVisible = (editText.hasFocus()
                        && editText.text.isEmpty()
                        && searchHistoryAdapter.trackList.isNotEmpty())
                if (editText.text.isEmpty()) {
                    foundNothingPlaceholder.isVisible = false
                    internetErrorPlaceholder.isVisible = false
                    trackList.clear()
                    tracksAdapter.notifyDataSetChanged()
                }
                searchDebounce()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        }
        editText.addTextChangedListener(simpleTextWatcher)
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
        editText.setText(savedText)
    }

    val search = {
        if (savedText.isNotEmpty()) {
            progressBar.isVisible = true
            foundNothingPlaceholder.isVisible = false
            internetErrorPlaceholder.isVisible = false
            trackInteractor.searchTracks(savedText, object: TrackInteractor.TrackConsumer<List<Track>?> {
                override fun consume(data: ConsumerData<List<Track>?>) {
                    handler.post {
                        progressBar.isVisible = false
                        trackList.clear()
                        when(data) {
                            is ConsumerData.Data -> {
                                if (data.value!!.isNotEmpty()) {
                                    trackList.addAll(data.value)
                                    tracksAdapter.notifyDataSetChanged()
                                }
                                if (data.value.isEmpty()) {
                                    foundNothingPlaceholder.isVisible = true
                                    tracksAdapter.notifyDataSetChanged()
                                }
                            }
                            is ConsumerData.Error -> {
                                internetErrorPlaceholder.isVisible = true
                                trackList.removeAll(trackList.toSet())
                                tracksAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            })
        }}

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_TEXT_DEF =""
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}