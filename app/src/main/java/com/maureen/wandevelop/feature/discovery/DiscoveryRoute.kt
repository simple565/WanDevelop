package com.maureen.wandevelop.feature.discovery

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object DiscoveryRoute

fun NavGraphBuilder.discoveryScreen(
    onFeedClick: (String) -> Unit,
    onCourseClick: (Int, Int) -> Unit
) {
    composable<DiscoveryRoute> {
        DiscoveryPage(
            onFeedClick = onFeedClick,
            onCourseClick = onCourseClick
        )
    }
}