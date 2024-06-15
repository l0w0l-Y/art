package com.kaleksandra.featuremain.image

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaleksandra.coredata.network.doOnSuccess
import com.kaleksandra.coredata.network.repository.ArtRepository
import com.kaleksandra.featuremain.hexToRgb
import com.kaleksandra.featuremain.stats.ImageStatsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OpenImageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val repository: ArtRepository,
) : ViewModel() {
    val stats = MutableStateFlow<ImageStatsModel?>(null)

    init {
        savedStateHandle.get<Long>("id")?.let {
            viewModelScope.launch {
                repository.getArt(it).doOnSuccess {
                    stats.emit(
                        ImageStatsModel(
                            id = it.id,
                            link = it.link,
                            name = it.name,
                            type = it.type,
                            style = it.style,
                            colors = it.colors.split(", ").map { hexToRgb(it) },
                            images = it.images.split(", ")
                        )
                    )
                }
            }
        }
    }
}