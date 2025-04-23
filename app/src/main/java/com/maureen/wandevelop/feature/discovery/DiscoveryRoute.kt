package com.maureen.wandevelop.feature.discovery

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.maureen.wandevelop.feature.discovery.composable.DiscoveryPage
import kotlinx.serialization.Serializable

@Serializable
object DiscoveryRoute

fun NavGraphBuilder.discoveryScreen(
    onFeedClick: (String) -> Unit
) {
    composable<DiscoveryRoute> {
        DiscoveryPage(
            onFeedClick = onFeedClick
        )
    }
}