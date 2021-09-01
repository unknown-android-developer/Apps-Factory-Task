package com.test.appsfactorytask.screen.tracks.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.test.appsfactorytask.R
import com.test.appsfactorytask.common.model.TrackModel

class TracksAdapter constructor(
    val tracks: List<TrackModel>
) : RecyclerView.Adapter<TracksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        tracks.getOrNull(position)?.let {
            holder.tvTrack.text = it.name
        }
    }

    override fun getItemCount(): Int = tracks.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvTrack: TextView = view.findViewById(R.id.tvTrack)
    }
}