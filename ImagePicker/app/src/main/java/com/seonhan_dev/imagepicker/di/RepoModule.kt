package com.seonhan_dev.imagepicker.di

import com.seonhan_dev.imagepicker.data.repository.GalleryRepository
import com.seonhan_dev.imagepicker.data.repository.GalleryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    @Singleton
    abstract fun provideGalleryRepository(
        dataRepository: GalleryRepositoryImpl
    ) : GalleryRepository
}