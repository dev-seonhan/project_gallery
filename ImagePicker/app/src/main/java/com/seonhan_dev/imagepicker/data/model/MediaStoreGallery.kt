package com.seonhan_dev.imagepicker.data.model

import android.net.Uri
import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.seonhan_dev.imagepicker.util.AppConst
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaStoreGallery(
    val id: Long,
    val displayName: String,
    val bucket: String,
    val duration: Int = 0,
    val contentUri: Uri
) : Parcelable {
    val isVideo: Boolean = displayName.contains(AppConst.MP4_EXT)

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<MediaStoreGallery>() {
            override fun areItemsTheSame(oldItem: MediaStoreGallery, newItem: MediaStoreGallery) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MediaStoreGallery, newItem: MediaStoreGallery) =
                oldItem == newItem
        }
    }
}
