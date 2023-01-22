package com.seonhan_dev.imagepicker.data.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.seonhan_dev.imagepicker.data.model.MediaStoreGallery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GalleryRepository {
    suspend fun getGalleryList(context: Context): List<MediaStoreGallery>
}

class GalleryRepositoryImpl @Inject constructor() : GalleryRepository {

    override suspend fun getGalleryList(context: Context): List<MediaStoreGallery> {
        val galleryList = mutableListOf<MediaStoreGallery>()
        galleryList += getVideoList(context)
        galleryList += getImageList(context)
        return galleryList
    }

    private suspend fun getVideoList(context: Context): List<MediaStoreGallery> {
        val videos = mutableListOf<MediaStoreGallery>()

        withContext(Dispatchers.IO) {
            val projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE
            )

            context.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val bucketColumn =
                    cursor.getColumnIndex(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val displayName = cursor.getString(nameColumn)
                    val bucket = cursor.getString(bucketColumn)
                    val duration = cursor.getInt(durationColumn)
                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    videos += MediaStoreGallery(
                        id = id,
                        displayName = displayName,
                        bucket = bucket,
                        duration = duration,
                        contentUri = contentUri
                    )
                }
            }
        }
        Log.i("TAG", "Added videos: $videos")
        return videos
    }

    private suspend fun getImageList(context: Context): List<MediaStoreGallery> {
        val images = mutableListOf<MediaStoreGallery>()

        withContext(Dispatchers.IO) {
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DISPLAY_NAME
            )

            context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val bucketColumn =
                    cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val bucket = cursor.getString(bucketColumn)
                    val displayName = cursor.getString(displayNameColumn)

                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    images += MediaStoreGallery(
                        id = id,
                        displayName = displayName,
                        bucket = bucket,
                        contentUri = contentUri
                    )
                }
            }
        }
        Log.i("TAG", "Added image: $images")
        return images
    }
}