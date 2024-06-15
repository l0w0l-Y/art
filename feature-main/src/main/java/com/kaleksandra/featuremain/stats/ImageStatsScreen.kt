package com.kaleksandra.featuremain.stats

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
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
    var isCameraOpened by remember { mutableStateOf(false) }
    var type by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { _uri: Uri? ->
        _uri?.let {
            context.contentResolver.takePersistableUriPermission(
                _uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            type = "gallery"
            uri = _uri
            //onImageSelected(_uri)
        }
    }
    LaunchedEffect(key1 = uri) {
        if (uri != Uri.EMPTY) {
            onImageSelected(uri)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimen.padding_16),
        verticalArrangement = Arrangement.spacedBy(Dimen.padding_8)
    ) {
        when (type) {
            null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            launcher.launch(arrayOf("image/*"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(Dimen.padding_4)) {
                            Icon(imageVector = Icons.Outlined.Photo, contentDescription = null)
                            Text(text = "Выбрать из галереи")
                        }
                    }
                    Button(
                        onClick = {
                            isCameraOpened = true
                            type = "camera"
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(Dimen.padding_4)) {
                            Icon(
                                imageVector = Icons.Outlined.PhotoCamera,
                                contentDescription = null
                            )
                            Text(text = "Сделать фото")
                        }
                    }
                    Button(
                        onClick = {
                            isCameraOpened = true
                            type = "camera"
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(Dimen.padding_4)) {
                            Icon(
                                imageVector = Icons.Outlined.Archive,
                                contentDescription = null
                            )
                            Text(text = "Добавить архив")
                        }
                    }
                }
            }

            else -> {
                if (isCameraOpened) {
                    CameraScreen {
                        //onImageSelected(it)
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

                        else -> {

                        }
                    }
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
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimen.padding_4),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center
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
        Text(
            text = "Идет анализ работы",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}