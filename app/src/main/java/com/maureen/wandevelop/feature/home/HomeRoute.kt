package com.maureen.wandevelop.feature.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

fun NavGraphBuilder.homeScreen(
    onSearchClick: () -> Unit,
    onFeedClick: (String) -> Unit
) {
    composable<HomeRoute> {
        HomeScreen(
            onFeedClick = onFeedClick,
            onSearchClick = onSearchClick
        )
    }
}