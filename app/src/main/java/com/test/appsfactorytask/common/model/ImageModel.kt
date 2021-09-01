package com.test.appsfactorytask.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageModel(val url: String, val size: String) : Parcelable
