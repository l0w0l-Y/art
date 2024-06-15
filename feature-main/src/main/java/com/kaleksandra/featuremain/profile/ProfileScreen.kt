package com.kaleksandra.featuremain.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    ProfileScreen(
        ProfileModel(
            name = "Kaleksandra",
            nickname = "Kaleksandra",
            arts = listOf("Art1", "Art2")
        )
    )
}

@Composable
fun ProfileScreen(
    profile: ProfileModel
) {
    Column {
        AsyncImage(
            model = "",
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
        )
        Column {
            Text(
                profile.name
            )
            Text(
                profile.nickname
            )
        }
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(profile.arts) {
                AsyncImage(model = it, contentDescription = null)
            }
        }
    }
}