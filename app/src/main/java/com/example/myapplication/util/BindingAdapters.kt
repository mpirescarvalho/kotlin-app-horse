package com.example.myapplication.util

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.myapplication.R

@BindingAdapter("backgroundActive")
fun View.setBackgroundActive(active: Boolean) {
    val color = if (active) R.color.active else R.color.inactive
    val bg = ContextCompat.getDrawable(this.context, R.drawable.rounded_background_grey) as GradientDrawable
    bg.setColor(ContextCompat.getColor(this.context, color))
    this.background = bg
}

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(imgUrl: String) {
    Glide.with(this.context)
        .load(imgUrl)
        .into(this)
}