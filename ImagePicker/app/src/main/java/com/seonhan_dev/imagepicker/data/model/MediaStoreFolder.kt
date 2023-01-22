package com.seonhan_dev.imagepicker.data.model

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil

data class MediaStoreFolder(
    val item: String,
    val count: Int,
    val contentUri: Uri
) {
    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<MediaStoreFolder>() {
            override fun areItemsTheSame(oldItem: MediaStoreFolder, newItem: MediaStoreFolder) =
                oldItem.item == newItem.item

            override fun areContentsTheSame(oldItem: MediaStoreFolder, newItem: MediaStoreFolder) =
                oldItem == newItem
        }
    }
}