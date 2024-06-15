package com.kaleksandra.coredata.network.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kaleksandra.coredata.entity.GalleryEntity

@Dao
interface GalleryDao {

    @Insert
    fun insertArt(art: GalleryEntity)

    @Query("SELECT * FROM gallery")
    fun getGallery(): List<GalleryEntity>

    @Query("SELECT * FROM gallery WHERE id = :id")
    fun getArt(id: Long): GalleryEntity
}