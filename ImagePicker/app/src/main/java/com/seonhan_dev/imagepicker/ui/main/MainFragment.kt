package com.seonhan_dev.imagepicker.ui.main

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.navigation.NavigationView
import com.seonhan_dev.imagepicker.R
import com.seonhan_dev.imagepicker.data.model.MediaStoreFolder
import com.seonhan_dev.imagepicker.databinding.FragmentMainBinding
import com.seonhan_dev.imagepicker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main),
    NavigationView.OnNavigationItemSelectedListener {

    companion object {

    }

    private val viewModel: MainViewModel by activityViewModels()
    lateinit var folders: List<MediaStoreFolder>

    private val mainAdapter by lazy {
        MainAdapter { itemClick ->
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment()
            findNavController().navigate(action)
            viewModel.setSelectedFolder(itemClick)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.galleryList.collect { galleryList ->
                    galleryList?.let {

                        folders = galleryList.reversed().map { item ->
                            MediaStoreFolder(
                                item.bucket,
                                galleryList.count { item.bucket == it.bucket },
                                galleryList.filter { item.bucket == it.bucket }[0].contentUri
                            )
                        }.distinct()

                        mainAdapter.submitList(folders)
                        Log.i("TAG", "folders : $folders")
                    }
                }
            }
        }
    }

    private fun initView() {

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)

        binding.folderContent.recyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = mainAdapter
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        return false
    }
}