package com.kaleksandra.composetemplate

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kaleksandra.corenavigation.GalleryDirection
import com.kaleksandra.corenavigation.MainDirection
import com.kaleksandra.corenavigation.OpenImageDirection
import com.kaleksandra.coretheme.AppTheme
import com.kaleksandra.featuremain.gallery.GalleryScreen
import com.kaleksandra.featuremain.image.OpenImageScreen
import com.kaleksandra.featuremain.stats.ImageStatsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val startDestination = GalleryDirection.path
                val navController = rememberNavController()
                Scaffold {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                    ) {
                        composable(MainDirection.path) {
                            ImageStatsScreen(navController = navController)
                        }
                        composable(GalleryDirection.path) {
                            GalleryScreen(navController = navController)
                        }
                        composable(
                            OpenImageDirection.path + "?id={id}",
                            arguments = listOf(navArgument("id") {
                                type = NavType.LongType
                            })
                        ) {
                            OpenImageScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun Main() {
        Text(
            text = "Hello Main!",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}