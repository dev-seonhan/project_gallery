package com.seonhan_dev.imagepicker.data.model

import androidx.recyclerview.widget.DiffUtil

data class SvgImage(
    val ImageSrc: String
) {
    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<SvgImage>() {
            override fun areItemsTheSame(oldItem: SvgImage, newItem: SvgImage) =
                oldItem.ImageSrc == newItem.ImageSrc

            override fun areContentsTheSame(oldItem: SvgImage, newItem: SvgImage) =
                oldItem == newItem
        }
    }
}