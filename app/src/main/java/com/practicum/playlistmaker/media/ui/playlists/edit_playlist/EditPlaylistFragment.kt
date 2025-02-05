package com.practicum.playlistmaker.media.ui.playlists.edit_playlist

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.domain.model.Playlist
import com.practicum.playlistmaker.media.ui.playlists.create_playlist.CreatePlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : CreatePlaylistFragment() {
    override val viewModel by viewModel<EditPlaylistViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlist = Gson().fromJson(arguments?.getString("playlist"), Playlist::class.java)

        binding.createButton.setText(R.string.save)
        binding.title.setText(playlist.title)
        binding.description.setText(playlist.description)
        Glide.with(requireActivity())
            .load(playlist.fileUri)
            .transform(
                CenterCrop(),
                RoundedCorners((8f * resources.displayMetrics.density).toInt())
            )
            .into(binding.cover)

        binding.createButton.setOnClickListener {
            playlist.title = binding.title.text.toString()
            playlist.description = binding.description.text.toString()
            if(fileUri != null) {
                playlist.fileUri = fileUri.toString()
            }
            viewModel.updatePlaylist(playlist)
            findNavController().navigateUp()
        }
    }

    override fun closeNav() {
        findNavController().navigateUp()
    }
}