package com.test.appsfactorytask.screen.searchartist.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.appsfactorytask.R
import com.test.appsfactorytask.common.model.ArtistModel
import com.test.appsfactorytask.common.util.getResDrawable

class ArtistAdapter(
    private val onItemClick: (ArtistModel) -> Unit
) : ListAdapter<ArtistModel, ArtistAdapter.ViewHolder>(ArtistDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        currentList
            .getOrNull(position)
            ?.let { artist ->
                holder.apply {
                    itemView.setOnClickListener { onItemClick(artist) }
                    tvArtist.text = artist.name
                    if (artist.image?.url.isNullOrEmpty()) {
                        ivArtist.setImageDrawable(ivArtist.getResDrawable(R.drawable.ic_no_pic))
                    } else {
                        Glide
                            .with(ivArtist)
                            .load(artist.image?.url)
                            .into(ivArtist)
                    }
                }
            }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val ivArtist: ImageView = view.findViewById(R.id.ivArtist)

        val tvArtist: TextView = view.findViewById(R.id.tvArtist)
    }

    private class ArtistDiffCallBack() : DiffUtil.ItemCallback<ArtistModel>() {

        override fun areItemsTheSame(
            oldItem: ArtistModel,
            newItem: ArtistModel
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: ArtistModel,
            newItem: ArtistModel
        ): Boolean = oldItem == newItem
    }
}