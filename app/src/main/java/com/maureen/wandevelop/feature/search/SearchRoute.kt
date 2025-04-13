package com.maureen.wandevelop.feature.search

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

/**
 * @author lianml
 * @date 2025/4/4
 */
@Serializable
data object SearchRoute

fun NavGraphBuilder.searchScreen(
    onBackClick: () -> Unit,
    onFeedClick: (String) -> Unit
) {
    composable<SearchRoute> {
        SearchScreen(
            onBackClick = onBackClick,
            onFeedClick = onFeedClick
        )
    }
}

fun NavHostController.navigateToSearch(navOptions: NavOptions? = null) = navigate(SearchRoute, navOptions)