package com.kaleksandra.featuremain.stats

import android.net.Uri
import androidx.compose.animation.core.EaseInOutBack
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kaleksandra.coretheme.AppTheme
import com.kaleksandra.coretheme.Dimen
import com.kaleksandra.featuremain.photo.CameraScreen
import com.taekwondo.featuremain.R

@Composable
fun ImageStatsScreen(
    navController: NavController,
    viewModel: ImageStatsViewModel = hiltViewModel()
) {
    val stats by viewModel.stats.collectAsState()
    val state by viewModel.state.collectAsState()
    ImageStatsScreen(
        stats,
        state,
        viewModel::sendImage
    )
}

@Composable
fun ImageStatsScreen(
    stats: ImageStatsModel,
    state: ImageStatsViewModel.State,
    onImageSelected: (Uri) -> Unit
) {
    var uri by remember { mutableStateOf(Uri.EMPTY) }
    var isCameraOpened by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimen.padding_16),
        verticalArrangement = Arrangement.spacedBy(Dimen.padding_8)
    ) {
        if (isCameraOpened) {
            CameraScreen {
                onImageSelected(it)
                uri = it
                isCameraOpened = false
            }
        } else {
            when (state) {
                ImageStatsViewModel.LoadingState -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LoadingAnimation(modifier = Modifier.align(Alignment.Center))
                    }
                }

                ImageStatsViewModel.SuccessState -> {
                    AsyncImage(
                        model = stats.link,
                        contentDescription = null,
                        modifier = Modifier.border(16.dp, Color(0xFF381F08), RectangleShape)
                    )
                    Text(text = stats.name)
                    Text(text = stats.type)
                    Text(text = stats.style)
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
                    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                        items(stats.images) {
                            AsyncImage(
                                model = it,
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                    Button(onClick = { onImageSelected(uri) }) {
                        Text(text = "Загрузить")
                    }
                }

                else -> {

                }
            }
        }
    }
}

@Composable
fun LoadingAnimation(modifier: Modifier = Modifier, speed: Int = 600, height: Dp = 60.dp) {
    val infiniteTransition = rememberInfiniteTransition("RotationTransition")
    val rotation by infiniteTransition.animateFloat(
        initialValue = -20f, targetValue = 20f, animationSpec = infiniteRepeatable(
            animation = tween(speed, easing = EaseInOutBack),
            repeatMode = RepeatMode.Reverse,
        ), label = "RotationCompletion"
    )
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = rotation
                }
                .height(height),
            painter = painterResource(id = R.drawable.ic_art),
            contentDescription = "",
            contentScale = ContentScale.FillHeight
        )
    }
}