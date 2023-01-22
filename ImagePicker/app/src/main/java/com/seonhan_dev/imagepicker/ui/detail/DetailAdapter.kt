package com.seonhan_dev.imagepicker.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seonhan_dev.imagepicker.data.model.MediaStoreGallery
import com.seonhan_dev.imagepicker.databinding.ItemImageBinding

class DetailAdapter(private var itemClick: (MediaStoreGallery) -> Unit) :
    ListAdapter<MediaStoreGallery, DetailAdapter.ViewHolder> (MediaStoreGallery.DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, holder)
        }
    }

    inner class ViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MediaStoreGallery, holder: ViewHolder) {
            with(binding) {
                Glide.with(albumImage)
                    .load(item.contentUri)
                    .centerCrop()
                    .into(albumImage)
            }

            itemView.setOnClickListener {
                itemClick(item)
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