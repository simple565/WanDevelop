package com.maureen.wandevelop.main.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.maureen.wandevelop.feature.discovery.course.courseDetailScreen
import com.maureen.wandevelop.feature.profile.profilerScreens
import com.maureen.wandevelop.feature.search.searchScreen
import com.maureen.wandevelop.feature.setting.settingScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WanDevelopApp(
    appState: WanDevAppState,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
            )
        },
    ) { padding ->
        NavHost(modifier = modifier, navController = navController, startDestination = IndexRoute) {
            feedDetailScreen(
                onBackClick = navController::popBackStack
            )
            courseDetailScreen(
                onBackClick = navController::popBackStack
            )
            indexScreen(
                appState = appState
            )
            searchScreen(
                onBackClick = navController::popBackStack,
                onFeedClick = navController::navigateToFeedDetail
            )
            profilerScreens(
                onBackClick = navController::popBackStack,
                onFeedClick = navController::navigateToFeedDetail
            )
            settingScreens(
                onBackClick = navController::popBackStack
            )
        }
    }
}