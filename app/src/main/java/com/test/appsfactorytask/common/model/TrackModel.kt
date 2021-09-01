package com.test.appsfactorytask.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackModel(val name: String, val albumId: Int) : Parcelable
