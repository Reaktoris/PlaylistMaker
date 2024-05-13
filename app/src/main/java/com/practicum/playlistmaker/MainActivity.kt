package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val mediaButton = findViewById<Button>(R.id.media_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)

        val searchActivityIntent = Intent(this, SearchActivity::class.java)

        val searchButtonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(searchActivityIntent)
            }
        }
        searchButton.setOnClickListener(searchButtonClickListener)

        mediaButton.setOnClickListener {
            val mediaActivityIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaActivityIntent) }

        settingsButton.setOnClickListener {
            val settingsActivityIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsActivityIntent)
        }
    }
}