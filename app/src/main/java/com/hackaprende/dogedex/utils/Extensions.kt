package com.hackaprende.dogedex.utils

import android.app.Activity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView

import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import coil.load
import coil.request.Disposable


fun Activity.changeColor(@ColorRes color: Int) = ContextCompat.getColor(this, color)

fun Any?.isNull() = this == null

fun TextView.changeSize(tam: Float) {
    return this.setTextSize(tam)
}

fun View.visible(isVisible: Boolean) {
    if (isVisible)
        this.visibility = VISIBLE
    else
        this.visibility = GONE
}

fun Activity.toast(text: String, timeToast: Int = Toast.LENGTH_SHORT) {
    return Toast.makeText(this, text, timeToast).show()
}



fun ImageView.loadImageCoin(url: String): Disposable {
    if (url.isNotEmpty())
        return this.load(url)
    else
        return this.load("")
}