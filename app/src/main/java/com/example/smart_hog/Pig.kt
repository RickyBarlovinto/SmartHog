package com.example.smart_hog

import android.graphics.Bitmap

data class Pig(
    val id: String,
    val batch: String,
    val weight: String,
    val status: String,
    var imageBitmap: Bitmap? = null,
    var imageResId: Int = R.drawable.pig4
)
