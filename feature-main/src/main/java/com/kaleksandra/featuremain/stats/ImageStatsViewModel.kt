package com.kaleksandra.featuremain.stats

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaleksandra.corecommon.ext.debug
import com.kaleksandra.coredata.network.doOnError
import com.kaleksandra.coredata.network.doOnSuccess
import com.kaleksandra.coredata.network.repository.ArtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ImageStatsViewModel @Inject constructor(
    val repository: ArtRepository
) : ViewModel() {
    val state = MutableStateFlow<State>(EmptyState)

    sealed class State
    object EmptyState : State()
    object LoadingState : State()
    object SuccessState : State()
    object ErrorState : State()

    val stats = MutableStateFlow<ImageStatsModel>(
        ImageStatsModel(
            link = "https://presenta.com.ua/wp-content/uploads/2020/08/gustav_klimt_zolota_adel-1024x1024.jpg",
            name = "Name",
            type = "Type",
            style = "Style",
            colors = listOf(Color.Green, Color.Yellow),
            images = listOf(
                "https://presenta.com.ua/wp-content/uploads/2020/08/gustav_klimt_zolota_adel-1024x1024.jpg",
                "https://presenta.com.ua/wp-content/uploads/2020/08/gustav_klimt_zolota_adel-1024x1024.jpg"
            )
        )
    )

    fun sendImage(uri: Uri) {
        viewModelScope.launch {
            state.emit(LoadingState)
            uri.path?.let {
                repository.sendPhoto(File(it)).doOnSuccess {
                    stats.emit(
                        ImageStatsModel(
                            link = it.link,
                            name = it.name,
                            type = it.type,
                            style = it.style,
                            colors = it.colors.map { hexToRgb(it) },
                            images = it.images
                        )
                    )
                    state.emit(SuccessState)
                }.doOnError {
                    debug(it)
                }
            }
        }
    }

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
}