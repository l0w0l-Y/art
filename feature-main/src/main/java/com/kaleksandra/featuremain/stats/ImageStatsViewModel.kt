package com.kaleksandra.featuremain.stats

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaleksandra.corecommon.ext.debug
import com.kaleksandra.coredata.network.doOnError
import com.kaleksandra.coredata.network.doOnSuccess
import com.kaleksandra.coredata.network.repository.ArtRepository
import com.kaleksandra.featuremain.hexToRgb
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
            id = 0,
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
                            id = 0,
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
}