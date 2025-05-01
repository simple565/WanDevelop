package com.maureen.wandevelop.main.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

/**
 * @author lianml
 * @date 2025/5/1
 */
@Serializable
data class FeedDetailRoute(val url: String)

fun NavGraphBuilder.feedDetailScreen(
    onBackClick: ()-> Unit
) {
    composable<FeedDetailRoute> { backStackEntry ->
        val url = backStackEntry.toRoute<FeedDetailRoute>().url
        FeedDetailPage(
            url = url,
            onBackClick = onBackClick
        )
    }
}

fun NavHostController.navigateToFeedDetail(url: String, navOptions: NavOptions? = null) {
    navigate(FeedDetailRoute(url = url), navOptions)
}