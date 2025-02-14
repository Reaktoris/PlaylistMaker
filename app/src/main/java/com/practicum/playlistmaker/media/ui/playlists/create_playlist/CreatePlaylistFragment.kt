package com.practicum.playlistmaker.media.ui.playlists.create_playlist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCreatePlaylistBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

open class CreatePlaylistFragment : Fragment() {
    lateinit var binding: FragmentCreatePlaylistBinding
    open val viewModel by viewModel<CreatePlaylistViewModel>()
    private val requester = PermissionRequester.instance()
    var fileUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if(it != null) {
                val imageName = viewModel.saveImage(it)
                fileUri = viewModel.loadImage(imageName)
                Glide.with(requireActivity())
                    .load(fileUri)
                    .transform(
                        CenterCrop(),
                        RoundedCorners((8f * resources.displayMetrics.density).toInt())
                    )
                    .into(binding.cover)
            }
        }

        binding.coverLayout.setOnClickListener {
            lifecycleScope.launch {
                requester.request(android.Manifest.permission.READ_MEDIA_IMAGES).collect { result ->
                    when (result) {
                        is PermissionResult.Granted -> {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                        is PermissionResult.Denied.DeniedPermanently -> {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.data = Uri.fromParts("package", requireContext().packageName, null)
                            requireContext().startActivity(intent)
                        }
                        is PermissionResult.Cancelled -> {
                            return@collect
                        }
                        else -> {}
                    }
                }
            }
        }

        binding.title.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    binding.createButton.isEnabled = s.isNotEmpty()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.createButton.setOnClickListener {
            viewModel.addPlaylist(
                title = binding.title.text.toString(),
                description = binding.description.text.toString(),
                fileUri = fileUri.toString()
            )
            findNavController().navigateUp()
            Toast.makeText(requireContext(),"Плейлист ${binding.title.text.toString()} создан", Toast.LENGTH_SHORT).show()
        }

        binding.toolbar.setNavigationOnClickListener {
            closeNav()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            closeNav()
        }
    }

    open fun closeNav() {
        if (!binding.title.text.isNullOrEmpty() ||
            !binding.description.text.isNullOrEmpty() ||
            fileUri != null) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message)
                .setNeutralButton(R.string.dialog_cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.dialog_quit){ _, _ ->
                    findNavController().navigateUp()
                }.show()
        } else {
            findNavController().navigateUp()
        }
    }

    companion object {
        fun newInstance() = CreatePlaylistFragment()
    }
}