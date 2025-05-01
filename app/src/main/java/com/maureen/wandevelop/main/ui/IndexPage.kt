package com.maureen.wandevelop.main.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.maureen.wandevelop.ext.isCurrentBottomNavDestination
import com.maureen.wandevelop.ext.navigateToBottomNavDestination
import com.maureen.wandevelop.feature.discovery.discoveryScreen
import com.maureen.wandevelop.feature.home.HomeRoute
import com.maureen.wandevelop.feature.home.homeScreen
import com.maureen.wandevelop.feature.profile.navigateToNotification
import com.maureen.wandevelop.feature.profile.navigateToSignInOrUp
import com.maureen.wandevelop.feature.profile.profileScreen
import com.maureen.wandevelop.feature.search.navigateToSearch
import kotlinx.serialization.Serializable

/**
 * @author lianml
 * @date 2025/4/6
 */
@Serializable
data object IndexRoute

fun NavGraphBuilder.indexScreen(
    appState: WanDevAppState,
) {
    composable<IndexRoute> {
        IndexPage(
            appState = appState
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun IndexPage(
    appState: WanDevAppState,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surfaceBright
            ) {
                WanDevAppState.bottomNavDestinations.forEach { destination ->
                    NavigationBarItem(
                        selected = navBackStackEntry?.isCurrentBottomNavDestination(destination) == true,
                        onClick = { navController.navigateToBottomNavDestination(destination) },
                        icon = {
                            Icon(
                                painter = painterResource(destination.iconId),
                                contentDescription = stringResource(id = destination.labelId)
                            )
                        },
                        label = {
                            Text(text = stringResource(id = destination.labelId))
                        }
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
            )
        },
    ) { padding ->
        IndexHost(
            appState = appState,
            navController = navController,
            onShowSnackBar = { message, action ->
                snackBarHostState.showSnackbar(
                    message = message,
                    actionLabel = action,
                    duration = SnackbarDuration.Short,
                ) == ActionPerformed
            },
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
        )
    }
}

@Composable
private fun IndexHost(
    appState: WanDevAppState,
    navController: NavHostController,
    onShowSnackBar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier
) {

    NavHost(modifier = modifier, navController = navController, startDestination = HomeRoute) {
        homeScreen(
            onSearchClick = appState.navController::navigateToSearch,
            onFeedClick = appState.navController::navigateToFeedDetail
        )
        discoveryScreen(
            onFeedClick = appState.navController::navigateToFeedDetail
        )
        profileScreen(
            otherEntranceList = WanDevAppState.otherEntranceList,
            onNotificationClick = { appState.navController.navigateToNotification(it) },
            onSignInOrUpClick = { appState.navController.navigateToSignInOrUp(isSignIn = it) },
            onEntranceItemClick = { appState.navigate(labelId = it) }
        )
    }
}