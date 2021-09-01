package com.test.appsfactorytask.screen.albums.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.appsfactorytask.R
import com.test.appsfactorytask.common.model.AlbumModel
import com.test.appsfactorytask.common.util.*
import com.test.appsfactorytask.screen.topalbums.view.TopAlbumsAdapter

class AlbumsAdapter(
    private val onAlbumClick: (AlbumModel) -> Unit,
    private val onDeleteClick: (AlbumModel) -> Unit
) : ListAdapter<AlbumModel, AlbumsAdapter.ViewHolder>(AlbumDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        currentList
            .getOrNull(position)
            ?.let { album ->
                holder.apply {
                    tvAlbum.text = album.name
                    tvArtist.text = album.author
                    ibDelete.apply {
                        setVisible()
                        background = getResDrawable(R.drawable.ic_delete_white)
                        setOnClickListener {
                            onDeleteClick(album)
                        }
                    }
                    itemView.setOnClickListener { onAlbumClick(album) }
                    if (album.image.isNotEmpty()) {
                        Glide
                            .with(ivAlbum)
                            .load(album.image)
                            .into(ivAlbum)
                    }
                }
            }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val ivAlbum: ImageView = view.findViewById(R.id.ivAlbum)

        val tvAlbum: TextView = view.findViewById(R.id.tvAlbum)

        val tvArtist: TextView = view.findViewById(R.id.tvArtist)

        val ibDelete: ImageButton = view.findViewById(R.id.ibSave)
    }
}