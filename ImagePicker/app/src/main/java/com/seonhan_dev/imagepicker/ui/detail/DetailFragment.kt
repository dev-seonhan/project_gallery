package com.seonhan_dev.imagepicker.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.seonhan_dev.imagepicker.R
import com.seonhan_dev.imagepicker.data.model.MediaStoreFolder
import com.seonhan_dev.imagepicker.databinding.FragmentDetailBinding
import com.seonhan_dev.imagepicker.ui.base.BaseFragment
import com.seonhan_dev.imagepicker.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    companion object {

    }

    private val mainViewModel: MainViewModel by activityViewModels()
    private val detailViewModel: DetailViewModel by viewModels()

    lateinit var folderItem: MediaStoreFolder

    private val detailAdapter by lazy {
        DetailAdapter { itemClick ->
            val action = DetailFragmentDirections.actionDetailFragmentToOverlayFragment()
            findNavController().navigate(action)
            mainViewModel.setSelectedImage(itemClick)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                mainViewModel.selectedFolder.collect { folderInfo ->
                    folderInfo?.let {
                        folderItem = it
                        detailAdapter.updateItem(folderItem.item, mainViewModel.galleryList.value!!)
                    }
                }

            }
        }
    }

    private fun initView() {

        val gridLayoutManager = GridLayoutManager(requireContext(), 3)

        with(binding) {
            viewmodel = mainViewModel

            albumContent.recyclerView.apply {
                layoutManager = gridLayoutManager
                adapter = detailAdapter
            }

            headlineBackImage.setOnClickListener {
                findNavController().navigateUp()
            }
        }

    }
}