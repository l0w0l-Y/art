package com.kaleksandra.coredata.network.model

data class ImageStatsDTO(
    val link: String,
    val name: String,
    val type: String,
    val style: String,
    val colors: List<String>,
    val images: List<String>,
)