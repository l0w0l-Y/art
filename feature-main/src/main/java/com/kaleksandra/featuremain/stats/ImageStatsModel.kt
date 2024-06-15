package com.kaleksandra.featuremain.stats

import androidx.compose.ui.graphics.Color

data class ImageStatsModel(
    val link: String,
    val name: String,
    val type: String,
    val style: String,
    val colors: List<Color>,
    val images: List<String>,
)