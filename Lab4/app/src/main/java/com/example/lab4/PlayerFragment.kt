package com.example.lab4

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class PlayerFragment : Fragment() {
    private val dataModel: DataModel by activityViewModels()
    private lateinit var playerView: PlayerView
    private lateinit var player: ExoPlayer
    private lateinit var uri: String
    private var playbackPosition: Long = 0
    private var playWhenReady: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player, container, false)

        playerView = view.findViewById(R.id.video_view)
        player = ExoPlayer.Builder(requireContext()).build()
        playerView.player = player

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val back = view.findViewById<ImageButton>(R.id.back)
        back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        uri = savedInstanceState?.getString("uri") ?: dataModel.uri.value.toString()
        playbackPosition = savedInstanceState?.getLong("playbackPosition") ?: 0
        playWhenReady = savedInstanceState?.getBoolean("playWhenReady") ?: true

        // uri = dataModel.uri.value.toString()
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
        player.seekTo(playbackPosition)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("uri", uri)
        outState.putLong("playbackPosition", player.currentPosition)
        outState.putBoolean("playWhenReady", player.playWhenReady)
    }

    override fun onStop() {
        super.onStop()
        player.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player.release()
        dataModel.uri.value = null
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            val playerPosition = savedInstanceState.getLong("playbackPosition")
            val isReady = savedInstanceState.getBoolean("playWhenReady")
            player.seekTo(playerPosition)
            if (isReady) {
                player.playWhenReady = true
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PlayerFragment()
    }
}
