package com.kaleksandra.coredata.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gallery")
data class GalleryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val link: String,
    val name: String,
    val type: String,
    val style: String,
    val colors: String,
    val images: String,
)