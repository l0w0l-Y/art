package com.kaleksandra.coredata.network.di

import com.kaleksandra.coredata.network.repository.ArtRepository
import com.kaleksandra.coredata.network.repository.ArtRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun provideRepository(impl: ArtRepositoryImpl): ArtRepository
}