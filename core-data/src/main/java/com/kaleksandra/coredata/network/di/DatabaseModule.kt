package com.kaleksandra.coredata.network.di

import android.content.Context
import androidx.room.Room
import com.kaleksandra.coredata.network.dao.GalleryDao
import com.kaleksandra.coredata.network.database.DataStoreProvider
import com.kaleksandra.coredata.network.database.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): DataStoreProvider = DataStoreProvider(context, dispatcher)

    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            "database"
        ).build()
    }

    @Provides
    fun provideEventDao(database: Database): GalleryDao {
        return database.galleryDao()
    }

}