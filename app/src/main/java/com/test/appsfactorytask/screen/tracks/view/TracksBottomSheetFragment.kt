package com.test.appsfactorytask.screen.tracks.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.test.appsfactorytask.R

class TracksBottomSheetFragment : BottomSheetDialogFragment() {

    private val args: TracksBottomSheetFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_tracks, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.album.let {
            val ivAlbum = view.findViewById<ImageView>(R.id.ivAlbum)
            if (it.image.isNotEmpty()) {
                Glide.with(ivAlbum).load(it.image).into(ivAlbum)
            }
            view.findViewById<TextView>(R.id.tvAlbum)?.text = it.name
            view.findViewById<TextView>(R.id.tvArtist)?.text = it.author
            view.findViewById<RecyclerView>(R.id.rvTracks)?.adapter = TracksAdapter(it.tracks)
        }
    }
}