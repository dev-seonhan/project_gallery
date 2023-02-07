package com.seonhan_dev.imagepicker.ui.overlay

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seonhan_dev.imagepicker.data.model.MediaStoreGallery
import com.seonhan_dev.imagepicker.databinding.ItemSvgImageBinding
import kotlinx.coroutines.selects.select

class GalleryAdapter(private val onClick: (MediaStoreGallery, Int) -> Unit) :
    ListAdapter<MediaStoreGallery, GalleryAdapter.ViewHolder>(MediaStoreGallery.DiffCallback) {

    var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSvgImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, holder)
            holder.itemView.isSelected = selectedPosition == position
            holder.itemView.setOnClickListener {
                if (selectedPosition >= 0) notifyItemChanged(selectedPosition)
                selectedPosition = holder.absoluteAdapterPosition
                notifyItemChanged(selectedPosition)
                onClick(getItem(selectedPosition), selectedPosition)
            }
        }
    }

    inner class ViewHolder(private val binding: ItemSvgImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MediaStoreGallery, holder: ViewHolder) {
            with(binding) {
                Glide.with(imageThumbnail)
                    .load(item.contentUri)
                    .thumbnail(0.33f)
                    .centerCrop()
                    .into(imageThumbnail)
            }
        }
    }

    fun updateItem(folderName: String, updateImageList: List<MediaStoreGallery>) {
        val updatedImageList = mutableListOf<MediaStoreGallery>()

        for (list in updateImageList) {
            if (list.bucket.contains(folderName)) {
                updatedImageList.add(list)
            }
        }

        submitList(updatedImageList)
    }
}