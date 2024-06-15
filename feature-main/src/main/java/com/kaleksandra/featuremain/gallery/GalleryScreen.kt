package com.kaleksandra.featuremain.gallery

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kaleksandra.corenavigation.MainDirection
import com.kaleksandra.corenavigation.OpenImageDirection
import com.kaleksandra.featuremain.stats.ImageStatsModel
import com.taekwondo.featuremain.R

@Composable
fun GalleryScreen(navController: NavController, viewModel: GalleryViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    GalleryScreen(
        models = state, { navController.navigate(MainDirection.path) },
        { navController.navigate(MainDirection.path) },
        { navController.navigate(OpenImageDirection.path + "?id=$it")}
    )
}

@Composable
fun GalleryScreen(
    models: List<ImageStatsModel> = emptyList(),
    onCreateArt: () -> Unit = {},
    onPickArt: () -> Unit = {},
    onOpenArt: (Long) -> Unit = {},
) {
    Column(
        modifier = with(Modifier) {
            fillMaxSize()
                .paint(
                    painterResource(id = R.drawable.image3),
                    contentScale = ContentScale.FillBounds
                )
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Галерея",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onPickArt() }) {
                Icon(
                    Icons.Outlined.AddPhotoAlternate,
                    contentDescription = "Добавить фото",
                )
            }
        }
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(100.dp),
            verticalItemSpacing = 20.dp,
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            content = {
                items(models) { model ->
                    AsyncImage(
                        model = model.link,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(8.dp, Color(0xFF140C04), RectangleShape)
                            .wrapContentHeight()
                            .clickable {
                                onOpenArt(model.id)
                            }
                    )
                }
            },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize()
        )
    }
}