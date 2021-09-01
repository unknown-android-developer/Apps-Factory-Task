package com.test.appsfactorytask.common.util

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources

fun View.getResDrawable(
    @DrawableRes drawableId: Int
): Drawable? = AppCompatResources.getDrawable(context, drawableId)

fun View.setVisible(isVisible: Boolean = true) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun View.setGone() { visibility = View.GONE }