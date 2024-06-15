package com.kaleksandra.featuremain.image

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kaleksandra.coretheme.Dimen
import com.kaleksandra.featuremain.stats.ImageStatsModel

@Composable
fun OpenImageScreen(
    navController: NavController,
    viewModel: OpenImageViewModel = hiltViewModel()
) {
    val stats by viewModel.stats.collectAsState()
    OpenImageScreen(stats)
}

@Composable
fun OpenImageScreen(stats: ImageStatsModel?) {
    stats?.let {
        Column(Modifier.fillMaxSize().padding(Dimen.padding_16)) {
            AsyncImage(
                model = stats.link,
                contentDescription = null,
                modifier = Modifier.border(16.dp, Color(0xFF381F08), RectangleShape)
            )
            Text(text = stats.type, style = MaterialTheme.typography.titleLarge)
            Text(text = stats.style, style = MaterialTheme.typography.titleMedium)
            Text(text = "Совпадения с ИИ не найдены")
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimen.padding_4)
            ) {
                items(stats.colors) {
                    Spacer(
                        modifier = Modifier
                            .size(16.dp)
                            .background(it, CircleShape)
                    )
                }
            }
            Text(
                "Похожие работы",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = Dimen.padding_8)
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(Dimen.padding_8)) {
                items(stats.images) {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        modifier = Modifier.height(100.dp),
                        contentScale = ContentScale.FillHeight
                    )
                }
            }
        }
    }
}