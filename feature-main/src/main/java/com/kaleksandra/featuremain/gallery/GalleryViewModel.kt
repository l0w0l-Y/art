package com.kaleksandra.featuremain.gallery

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
class GalleryViewModel @Inject constructor(
    val repository: ArtRepository,
) : ViewModel() {

    val state = MutableStateFlow<List<ImageStatsModel>>(emptyList())

    init {
        viewModelScope.launch {
            repository.getGallery().doOnSuccess {
                state.emit(
                    it.map {
                        ImageStatsModel(
                            link = it.link,
                            name = it.name,
                            type = it.type,
                            style = it.style,
                            colors = it.colors.split(", ").map { hexToRgb(it) },
                            images = it.images.split(", ")
                        )
                    }
                )
            }
        }
    }
}