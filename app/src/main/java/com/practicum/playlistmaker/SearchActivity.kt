package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val iTunesSearchBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesSearchBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesSearchService = retrofit.create(ITunesSearchApi::class.java)

    private lateinit var listener: OnSharedPreferenceChangeListener
    private lateinit var searchHistory: SearchHistory
    val trackList: MutableList<Track> = mutableListOf()
    private var savedText = SEARCH_TEXT_DEF
    private lateinit var searchText: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchHistoryRecycler: RecyclerView
    private lateinit var editText: EditText
    private lateinit var tracksAdapter: ItemsAdapter
    private lateinit var searchHistoryAdapter: ItemsAdapter
    private lateinit var foundNothingPlaceholder: LinearLayout
    private lateinit var internetErrorPlaceholder: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val sharedPreferences = getSharedPreferences(PREF, MODE_PRIVATE)

        searchHistory = SearchHistory(sharedPreferences)

        recyclerView = findViewById(R.id.recyclerView)
        tracksAdapter = ItemsAdapter()
        tracksAdapter.searchHistory = searchHistory
        tracksAdapter.trackList = trackList
        tracksAdapter.context = this@SearchActivity
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = tracksAdapter

        searchHistoryRecycler = findViewById(R.id.search_history_recycler)
        searchHistoryAdapter = ItemsAdapter()
        searchHistoryAdapter.searchHistory = searchHistory
        searchHistoryAdapter.trackList = searchHistory.getTrackList()
        searchHistoryAdapter.context = this@SearchActivity
        searchHistoryRecycler.layoutManager = LinearLayoutManager(this)
        searchHistoryRecycler.adapter = searchHistoryAdapter

        editText = findViewById(R.id.edit_text_id)
        val closeButton = findViewById<ImageView>(R.id.close_button)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        foundNothingPlaceholder = findViewById(R.id.found_nothing_placeholder)
        internetErrorPlaceholder = findViewById(R.id.internet_error_placeholder)
        val refreshButton = findViewById<Button>(R.id.refresh_button)
        val searchHistoryLayout = findViewById<LinearLayout>(R.id.search_history)
        val clearHistoryButton = findViewById<Button>(R.id.clear_history_button)


        listener = OnSharedPreferenceChangeListener { _, key ->
            if (key == SEARCHED_TRACKS_KEY) {
                searchHistoryAdapter.trackList = searchHistory.getTrackList()
                searchHistoryAdapter.notifyDataSetChanged()
            }
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        clearHistoryButton.setOnClickListener {
            searchHistory.clearHistory()
            searchHistoryAdapter.trackList = searchHistory.getTrackList()
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
            search()
        }

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchText = savedText
                search()
                true
            }
            false
        }



        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
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
            }
            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        editText.addTextChangedListener(simpleTextWatcher)
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_TEXT_DEF =""
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
        if (searchText.isNotEmpty()) {
            foundNothingPlaceholder.isVisible = false
            internetErrorPlaceholder.isVisible = false
            iTunesSearchService.search(searchText).enqueue(object : Callback<TrackResponse> {
                override fun onResponse(call: Call<TrackResponse>,
                                        response: Response<TrackResponse>) {
                    if (response.code() == 200) {
                        trackList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.results!!)
                            tracksAdapter.notifyDataSetChanged()
                        }
                        if (trackList.isEmpty()) {
                            foundNothingPlaceholder.isVisible = true
                            tracksAdapter.notifyDataSetChanged()
                        }
                    } else {
                        internetErrorPlaceholder.isVisible = true
                        trackList.removeAll(trackList)
                        tracksAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    internetErrorPlaceholder.isVisible = true
                    trackList.removeAll(trackList)
                    tracksAdapter.notifyDataSetChanged()
                }

            })
        }}

}