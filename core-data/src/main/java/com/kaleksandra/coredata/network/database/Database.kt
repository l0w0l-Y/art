package com.kaleksandra.coredata.network.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.kaleksandra.coredata.entity.GalleryEntity
import com.kaleksandra.coredata.network.dao.GalleryDao

@Database(
    entities = [
        GalleryEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun galleryDao(): GalleryDao
}

class DatabaseError(message: String?, cause: Throwable? = null) : Exception(message, cause)
