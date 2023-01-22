package com.seonhan_dev.imagepicker.util.extension

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

@BindingAdapter("setImageRes")
fun bindSetImageRes(imageView: ImageView, src: String?) {
    if (src == null) return

    Glide.with(imageView.context)
        .load(src)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .into(imageView)
}