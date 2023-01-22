package com.seonhan_dev.imagepicker.ui.overlay

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seonhan_dev.imagepicker.data.model.SvgImage
import com.seonhan_dev.imagepicker.databinding.ItemSvgImageBinding

class OverlayAdapter(private var itemClick: (SvgImage) -> Unit) :
    ListAdapter<SvgImage, OverlayAdapter.ViewHolder>(SvgImage.DiffCallback) {

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
        }
    }

    inner class ViewHolder(private val binding: ItemSvgImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(items: SvgImage, holder: ViewHolder) {
            with(binding) {
                Log.i("TAG", "item : ${items.ImageSrc}")

//                Glide.with(svgImage)
//                    .load(items.ImageSrc)
//                    .into(svgImage)
            }
            itemView.setOnClickListener {
                itemClick(items)
            }
        }
    }
}


