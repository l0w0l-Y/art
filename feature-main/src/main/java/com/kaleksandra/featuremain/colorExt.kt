package com.kaleksandra.featuremain

import androidx.compose.ui.graphics.Color

fun hexToRgb(hex: String): Color {
    // Убедимся, что строка HEX имеет правильный формат
    val hexRegex = Regex("^#?([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})$")
    val matchResult = hexRegex.matchEntire(hex)

    if (matchResult != null) {
        // Извлекаем значения красного, зеленого и синего каналов
        val red = matchResult.groupValues[1].toInt(16)
        val green = matchResult.groupValues[2].toInt(16)
        val blue = matchResult.groupValues[3].toInt(16)

        return Color(red, green, blue)
    }
    return Color.White
}