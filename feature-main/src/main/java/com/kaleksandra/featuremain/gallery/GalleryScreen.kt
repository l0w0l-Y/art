package com.kaleksandra.featuremain.gallery

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.taekwondo.featuremain.R

@Composable
fun GalleryScreen() {
    GalleryScreen(
        images = listOf(
            "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Vrubel_Demon.jpg/425px-Vrubel_Demon.jpg",
            "https://cs12.pikabu.ru/post_img/2022/01/13/5/1642059717191632422.jpg",
            "https://artchive.ru/res/media/img/oy800/work/454/348161@2x.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTI0RrjvKFlmRmL7R7JT2J0kE4-5w_eQh4vCQ&s",
        )
    )
}

@Composable
fun GalleryScreen(
    images: List<String> = emptyList(),
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
        Text(
            text = "Галерея",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 12.dp, start = 20.dp, end = 20.dp)
        )
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(100.dp),
            verticalItemSpacing = 20.dp,
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            content = {
                items(images) { photo ->
                    AsyncImage(
                        model = photo,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(8.dp, Color(0xFF140C04), RectangleShape)
                            .wrapContentHeight()
                    )
                }
            },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize()
        )
    }
}