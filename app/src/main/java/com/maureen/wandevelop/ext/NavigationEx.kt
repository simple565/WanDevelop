package com.maureen.wandevelop.ext

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.maureen.wandevelop.core.entity.WanDevNavDestination

/**
 * 跳转到指定底部导航Tab
 */
fun <T: Any> NavHostController.navigateToBottomNavDestination(destination: WanDevNavDestination<T>) {
    val option = navOptions {
        popUpTo(this@navigateToBottomNavDestination.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
    this.navigate(route = destination.route, navOptions = option)
}

@Composable
fun <T : Any> NavBackStackEntry.isCurrentBottomNavDestination(destination: WanDevNavDestination<T>): Boolean {
    return this.destination.hierarchy.any { it.hasRoute(route = destination.route::class) } == true
}