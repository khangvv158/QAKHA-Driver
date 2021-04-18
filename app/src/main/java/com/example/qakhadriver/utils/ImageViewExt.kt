package com.example.qakhadriver.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.qakhadriver.R

fun ImageView.loadUrl(url: String) {
    Glide.with(context)
        .load(url)
        .centerCrop()
        .placeholder(R.drawable.ic_placeholder)
        .into(this)
}
