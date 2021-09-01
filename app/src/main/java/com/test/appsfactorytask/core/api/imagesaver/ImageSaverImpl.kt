package com.test.appsfactorytask.core.api.imagesaver

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.bumptech.glide.Glide
import com.test.appsfactorytask.core.di.qualifier.AppContext
import com.test.appsfactorytask.core.di.qualifier.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.RuntimeException
import java.util.concurrent.ExecutionException
import javax.inject.Inject

class ImageSaverImpl @Inject constructor(
    @AppContext private val appContext: Context
) : ImageSaver {

    override suspend fun saveImage(url: String): Uri =
        if (url.isNotEmpty()) {
            var fos: FileOutputStream? = null
            try {
                val bitmap = Glide.with(appContext).asBitmap().load(url).submit().get()
                val imageDir = appContext.getDir(IMAGE_FOLDER, Context.MODE_PRIVATE)
                val imageFile = File(imageDir, Uri.parse(url).lastPathSegment ?: "").also {
                    it.createNewFile()
                }
                fos = FileOutputStream(imageFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                Uri.fromFile(imageFile)
            } catch (ie: InterruptedException) {
                Log.e(TAG, ie.toString())
                throw RuntimeException(ie)
            } catch (ee: ExecutionException) {
                Log.e(TAG, ee.toString())
                throw RuntimeException(ee)
            } finally {
                try {
                    fos?.let {
                        it.flush()
                        it.close()
                    }
                } catch (ioe: IOException) {
                    Log.e(TAG, ioe.toString())
                    throw RuntimeException(ioe)
                }
            }
        } else {
            Uri.EMPTY
        }

    override suspend fun deleteImage(url: String) {
        File(Uri.parse(url).path.orEmpty()).apply {
            if (exists()) {
                delete()
            }
        }
    }

    companion object {

        private const val TAG = "ImageSaverImpl"

        private const val IMAGE_FOLDER = "images"
    }
}