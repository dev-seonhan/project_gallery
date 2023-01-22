package com.seonhan_dev.imagepicker.ui.main

import android.app.Application
import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.seonhan_dev.imagepicker.data.model.MediaStoreFolder
import com.seonhan_dev.imagepicker.data.model.MediaStoreGallery
import com.seonhan_dev.imagepicker.data.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: GalleryRepository,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    private var contentObserver: ContentObserver? = null

    private val _galleryList = MutableStateFlow<List<MediaStoreGallery>?>(null)
    val galleryList: StateFlow<List<MediaStoreGallery>?> get() = _galleryList

    private val _selectedFolder = MutableStateFlow<MediaStoreFolder?>(null)
    val selectedFolder: StateFlow<MediaStoreFolder?> get() = _selectedFolder

    private val _selectedImage = MutableStateFlow<MediaStoreGallery?>(null)
    val selectedImage: StateFlow<MediaStoreGallery?> get() = _selectedImage

    fun loadImages() {
        viewModelScope.launch {
            _galleryList.value = repository.getGalleryList(context)

            if (contentObserver == null) {
                contentObserver = getApplication<Application>().contentResolver.registerObserver(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                ) {
                    loadImages()
                }
            }
        }
    }

    fun setSelectedFolder(folderInfo: MediaStoreFolder) {
        viewModelScope.launch {
            _selectedFolder.value = folderInfo
        }
    }

    fun setSelectedImage(imageInfo: MediaStoreGallery) {
        viewModelScope.launch {
            _selectedImage.value = imageInfo
        }
    }

    /**
     * Convenience extension method to register a [ContentObserver] given a lamda.
     */
    private fun ContentResolver.registerObserver(
        uri: Uri,
        observer: (selfChange: Boolean) -> Unit
    ): ContentObserver {
        val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                observer(selfChange)
            }
        }
        registerContentObserver(uri, true, contentObserver)
        return contentObserver
    }
}