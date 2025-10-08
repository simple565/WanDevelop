package com.maureen.wandevelop.feature.discovery.course

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class CourseDetailRoute(val parentId: Int, val courseId: Int)

fun NavGraphBuilder.courseDetailScreen(
    onBackClick: () -> Unit,
    onFeedClick: (String) -> Unit
) {
    composable<CourseDetailRoute> { backStackEntry ->
        CourseDetailScreen(
            parentId = backStackEntry.toRoute<CourseDetailRoute>().parentId,
            courseId = backStackEntry.toRoute<CourseDetailRoute>().courseId,
            onBackClick = onBackClick,
            onFeedClick = onFeedClick,
        )
    }
}

fun NavHostController.navigateToCourseDetail(parentId: Int, courseId: Int, navOptions: NavOptions? = null) {
    navigate(CourseDetailRoute(parentId = parentId, courseId = courseId), navOptions)
}