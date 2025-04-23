package com.maureen.wandevelop.main.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import com.maureen.wandevelop.R
import com.maureen.wandevelop.common.entity.WanDevNavDestination
import com.maureen.wandevelop.feature.discovery.DiscoveryRoute
import com.maureen.wandevelop.feature.home.HomeRoute
import com.maureen.wandevelop.feature.profile.BookmarkRoute
import com.maureen.wandevelop.feature.profile.ProfileRoute
import com.maureen.wandevelop.feature.profile.ReadRecordRoute
import com.maureen.wandevelop.feature.profile.SignInOrUpRoute
import com.maureen.wandevelop.feature.setting.AboutRoute
import com.maureen.wandevelop.feature.setting.SettingRoute

/**
 * @date 2025/4/4
 */
@Composable
fun rememberWanDevAppState(
    controller: NavHostController = rememberNavController()
): WanDevAppState {
    return WanDevAppState(
        navController = controller
    )
}

@Stable
class WanDevAppState(
    val navController: NavHostController
) {
    companion object {
        val bottomNavDestinations = listOf(
            WanDevNavDestination(
                iconId = R.drawable.ic_home_normal,
                labelId = R.string.nav_home,
                route = HomeRoute
            ),
            WanDevNavDestination(
                iconId = R.drawable.ic_discovery_normal,
                labelId = R.string.nav_discovery,
                route = DiscoveryRoute
            ),
            WanDevNavDestination(
                iconId = R.drawable.ic_profile,
                labelId = R.string.nav_profile,
                route = ProfileRoute
            )
        )
        val otherEntranceList = listOf(
            WanDevNavDestination(
                iconId = R.drawable.ic_arrow_right,
                labelId = R.string.nav_my_bookmark,
                route = ProfileRoute
            ),
            /*WanDevNavDestination(
                iconId = R.drawable.ic_arrow_right,
                labelId = R.string.nav_my_share,
                route = ProfileRoute
            ),*/
            WanDevNavDestination(
                iconId = R.drawable.ic_arrow_right,
                labelId = R.string.nav_read_record,
                route = ProfileRoute
            ),
            /*WanDevNavDestination(
                iconId = R.drawable.ic_arrow_right,
                labelId = R.string.nav_todo,
                route = ProfileRoute
            ),*/
            WanDevNavDestination(
                iconId = R.drawable.ic_arrow_right,
                labelId = R.string.nav_settings,
                route = ProfileRoute
            ),
            WanDevNavDestination(
                iconId = R.drawable.ic_arrow_right,
                labelId = R.string.nav_about_us,
                route = ProfileRoute
            )
        )
    }

    fun navigate(labelId: Int) {
        navController.navigateToTarget(labelId)
    }

    private fun NavHostController.navigateToTarget(labelId: Int, navOptions: NavOptions? = null) {
        when (labelId) {
            R.string.prompt_coin -> navigate(SignInOrUpRoute(isSignIn = true), navOptions)
            R.string.nav_my_bookmark -> navigate(BookmarkRoute, navOptions)
            // R.string.nav_my_share -> navigate(SignInOrUpRoute(isSignIn = true), navOptions)
            R.string.nav_read_record -> navigate(ReadRecordRoute, navOptions)
            R.string.nav_about_us -> navigate(AboutRoute, navOptions)
            R.string.nav_settings -> navigate(SettingRoute, navOptions)
            else -> {}
        }
    }
}