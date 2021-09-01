package com.test.appsfactorytask.common.util

import androidx.recyclerview.widget.DiffUtil
import com.test.appsfactorytask.common.model.AlbumModel

class AlbumDiffCallBack : DiffUtil.ItemCallback<AlbumModel>() {

    override fun areItemsTheSame(
        oldItem: AlbumModel,
        newItem: AlbumModel
    ): Boolean = (newItem.id != NO_ID && oldItem.id == newItem.id)
            || (oldItem.mbid == newItem.mbid && oldItem.name == newItem.name)

    override fun areContentsTheSame(
        oldItem: AlbumModel,
        newItem: AlbumModel
    ): Boolean = oldItem.id == newItem.id
            && oldItem.mbid == newItem.mbid
            && oldItem.name == newItem.name
            && oldItem.image == newItem.image
            && oldItem.isSaved == newItem.isSaved
            && oldItem.isSaving == newItem.isSaving
            && (oldItem.tracks.isNotEmpty() && newItem.tracks.isNotEmpty())

    companion object {

        private const val NO_ID = 0
    }
}