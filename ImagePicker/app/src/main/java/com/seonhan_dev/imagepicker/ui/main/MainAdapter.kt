package com.seonhan_dev.imagepicker.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.seonhan_dev.imagepicker.data.model.MediaStoreFolder
import com.seonhan_dev.imagepicker.databinding.ItemAlbumThumbBinding

class MainAdapter(private var itemClick: (MediaStoreFolder) -> Unit) :
    ListAdapter<MediaStoreFolder, MainAdapter.ViewHolder> (MediaStoreFolder.DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlbumThumbBinding.inflate(
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

    inner class ViewHolder(private val binding: ItemAlbumThumbBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(items: MediaStoreFolder, holder: ViewHolder) {
            with(binding) {

                Log.i("TAG", "item : ${items.item}")

                albumName.text = items.item
                albumCount.text = items.count.toString() + " Images"

                Glide.with(albumThumbnail)
                    .load(items.contentUri)
                    .transform(
                        CenterCrop(),
                        GranularRoundedCorners(27f, 27f, 0f, 0f)
                    )
                    .into(albumThumbnail)
            }

            itemView.setOnClickListener {
                itemClick(items)
            }
        }
    }

}