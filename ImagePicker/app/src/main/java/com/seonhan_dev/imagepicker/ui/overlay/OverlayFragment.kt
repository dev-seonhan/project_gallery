package com.seonhan_dev.imagepicker.ui.overlay

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.seonhan_dev.imagepicker.R
import com.seonhan_dev.imagepicker.data.model.MediaStoreFolder
import com.seonhan_dev.imagepicker.data.model.MediaStoreGallery
import com.seonhan_dev.imagepicker.databinding.FragmentOverlayBinding
import com.seonhan_dev.imagepicker.ui.base.BaseFragment
import com.seonhan_dev.imagepicker.ui.main.MainViewModel
import com.seonhan_dev.imagepicker.util.OffsetItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OverlayFragment : BaseFragment<FragmentOverlayBinding>(R.layout.fragment_overlay) {

    companion object {

    }

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var galleryItem: MediaStoreGallery
    private lateinit var galleryItemList: List<MediaStoreGallery>
    private lateinit var galleryList: MediaStoreFolder
    private lateinit var offsetITemDecorator: OffsetItemDecoration
    private lateinit var linearLayoutManager: LinearLayoutManager

    private val galleryAdapter by lazy {
        GalleryAdapter { selectedItem: MediaStoreGallery, selectedPosition: Int ->
            updateLayout(selectedItem, selectedPosition)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryItem = mainViewModel.selectedImage.value!!
        galleryAdapter.submitList(mainViewModel.galleryList.value)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                mainViewModel.selectedFolder.collect { folderInfo ->
                    folderInfo?.let {
                        galleryList = it
                        galleryAdapter.updateItem(galleryList.item, mainViewModel.galleryList.value!!)
                    }
                }

            }
        }

        initView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {

        with(binding) {
            viewmodel = mainViewModel

            imageContent.apply {
                linearLayoutManager = LinearLayoutManager(requireContext()).apply {
                    orientation = LinearLayoutManager.HORIZONTAL
                }

                offset = (context.resources.displayMetrics.widthPixels - dp2px(40)) / 2

                offsetITemDecorator = OffsetItemDecoration(R.dimen.item_offset)
                this.addItemDecoration(offsetITemDecorator)

                adapter = galleryAdapter
                layoutManager = linearLayoutManager

                galleryItemList = galleryAdapter.currentList
                galleryItemList.let {
                    it.forEachIndexed { index, _ ->
                        if (it[index].id == galleryItem.id) {
                            galleryAdapter.selectedPosition = index
                            linearLayoutManager.scrollToPositionWithOffset(index, offset)
                        }
                    }
                }
            }

            selectImage.apply {
                setOnTouchListener { _, ev ->
                    if (ev.action == MotionEvent.ACTION_UP) {
                        if (galleryCoverLayout.isVisible) {
                            hideGalleryCover()
                        } else {
                            showGalleryCover()
                        }
                    }
                    true
                }
            }

            headlineBackImage.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun updateLayout(item: MediaStoreGallery, position: Int) {
        linearLayoutManager.scrollToPositionWithOffset(position, offset)
        binding.galleryCoverLayout.visibility = View.VISIBLE

        mainViewModel.setSelectedImage(item)
        galleryItem = mainViewModel.selectedImage.value!!
    }

    private fun dp2px(dpValue: Int) : Int {
        val density = resources.displayMetrics.density
        return (dpValue * density).toInt()
    }

    private fun showGalleryCover() {
        binding.galleryCoverLayout.visibility = View.VISIBLE
    }

    private fun hideGalleryCover() {
        binding.galleryCoverLayout.visibility = View.INVISIBLE
    }

}