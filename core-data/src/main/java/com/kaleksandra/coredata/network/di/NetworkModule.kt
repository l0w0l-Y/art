package com.kaleksandra.coredata.network.di

import com.kaleksandra.coredata.network.api.ArtApi
import com.kaleksandra.coredata.network.builder
import com.kaleksandra.coredata.network.client
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideCollectionApi(): ArtApi =
        builder().client().build().create(ArtApi::class.java)
}