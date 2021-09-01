package com.test.appsfactorytask.core.api.imagesaver

import android.net.Uri

interface ImageSaver {

    suspend fun saveImage(url: String): Uri

    suspend fun deleteImage(url: String)
}