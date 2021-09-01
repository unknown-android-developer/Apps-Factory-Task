package com.test.appsfactorytask.screen.topalbums.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.appsfactorytask.R
import com.test.appsfactorytask.common.model.AlbumModel
import com.test.appsfactorytask.common.util.AlbumDiffCallBack
import com.test.appsfactorytask.common.util.getResDrawable
import com.test.appsfactorytask.common.util.setGone
import com.test.appsfactorytask.common.util.setVisible

class TopAlbumsAdapter constructor(
    private val onAlbumClick: (AlbumModel) -> Unit,
    private val onSaveClick: (AlbumModel) -> Unit
) : ListAdapter<AlbumModel, TopAlbumsAdapter.ViewHolder>(AlbumDiffCallBack()) {

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
                    ibSave.apply {
                        setVisible()
                        background = if (album.isSaved) {
                            getResDrawable(R.drawable.ic_delete_white)
                        } else {
                            getResDrawable(R.drawable.ic_save_white)
                        }
                        setOnClickListener {
                            onSaveClick(album)
                        }
                    }
                    if (album.isSaving) {
                        pbView.setVisible()
                    } else {
                        pbView.setGone()
                    }
                    itemView.setOnClickListener { onAlbumClick(album) }
                    if (album.image.isNotEmpty()) {
                        Glide
                            .with(ivAlbum)
                            .load(album.image)
                            .into(ivAlbum)
                    } else {
                        ivAlbum.setImageDrawable(ivAlbum.getResDrawable(R.drawable.ic_no_pic))
                    }
                }
            }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val ivAlbum: ImageView = view.findViewById(R.id.ivAlbum)

        val tvAlbum: TextView = view.findViewById(R.id.tvAlbum)

        val tvArtist: TextView = view.findViewById(R.id.tvArtist)

        val ibSave: ImageButton = view.findViewById(R.id.ibSave)

        val pbView: View = view.findViewById(R.id.pbView)
    }
}