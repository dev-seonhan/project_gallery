package com.seonhan_dev.imagepicker.ui.overlay

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.gson.Gson
import com.seonhan_dev.imagepicker.R
import com.seonhan_dev.imagepicker.data.model.SvgData
import com.seonhan_dev.imagepicker.data.model.SvgImage
import com.seonhan_dev.imagepicker.databinding.FragmentOverlayBinding
import com.seonhan_dev.imagepicker.ui.base.BaseFragment
import com.seonhan_dev.imagepicker.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class OverlayFragment : BaseFragment<FragmentOverlayBinding>(R.layout.fragment_overlay) {

    companion object {
        private const val IMAGE_JSON = "main.json"
    }

    private val mainViewModel: MainViewModel by activityViewModels()
    private val overlayViewModel: OverlayViewModel by viewModels()

    lateinit var jsonData: SvgData
    lateinit var svgImageItems: List<SvgImage>

    private val overlayAdapter by lazy {
        OverlayAdapter { itemClick ->
            // over lay image click action
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jsonData = getJsonData(IMAGE_JSON)
        svgImageItems = jsonData.svgImageList.map { item ->
            SvgImage(item.ImageSrc)
        }
        overlayAdapter.submitList(svgImageItems)

        Log.i("TAG", "svgImageItems : $svgImageItems")
        initView()

    }

    private fun initView() {

        val linearLayoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }

        with(binding) {
            viewmodel = mainViewModel

            svgContent.recyclerView.apply {
                layoutManager = linearLayoutManager
                adapter = overlayAdapter
            }

            headlineBackImage.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun getJsonData(fileName: String): SvgData {
        var image: SvgData? = null

        val assetManager = requireContext().resources.assets
        try {
            val inputStream = assetManager.open(fileName)
            val reader = inputStream.bufferedReader()
            val gson = Gson()
            image = gson.fromJson(reader, SvgData::class.java)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return image!!
    }

}