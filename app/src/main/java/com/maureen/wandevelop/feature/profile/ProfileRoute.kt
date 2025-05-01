package com.maureen.wandevelop.feature.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.maureen.wandevelop.core.entity.WanDevNavDestination
import com.maureen.wandevelop.feature.profile.bookmark.ui.BookmarkScreen
import com.maureen.wandevelop.feature.profile.bookmark.ui.ReadRecordScreen
import com.maureen.wandevelop.feature.profile.notification.NotificationScreen
import com.maureen.wandevelop.feature.profile.sign.SignInOrUpScreen
import kotlinx.serialization.Serializable

@Serializable
object ProfileRoute

@Serializable
object BookmarkRoute

@Serializable
data class SignInOrUpRoute(val isSignIn: Boolean)

@Serializable
data class NotificationRoute(val unreadMsgCount: Int)

@Serializable
data object ReadRecordRoute

fun NavGraphBuilder.profileScreen(
    otherEntranceList: List<WanDevNavDestination<ProfileRoute>>,
    onNotificationClick: (Int) -> Unit,
    onSignInOrUpClick: (Boolean) -> Unit,
    onEntranceItemClick: (Int) -> Unit
) {
    composable<ProfileRoute> {
        ProfileScreen(
            otherEntranceList = otherEntranceList,
            notificationClick = onNotificationClick,
            signInClick = { onSignInOrUpClick(true) },
            entranceItemClick = onEntranceItemClick
        )
    }
}

fun NavGraphBuilder.profilerScreens(
    onBackClick: () -> Unit,
    onFeedClick: (String) -> Unit
){
    composable<BookmarkRoute> { backStackEntry ->
        BookmarkScreen(
            onBackClick = onBackClick,
            onBookmarkClick = onFeedClick
        )
    }
    composable<SignInOrUpRoute> { backStackEntry ->
        val isSignInMode = backStackEntry.toRoute<SignInOrUpRoute>().isSignIn
        SignInOrUpScreen(
            isSignInMode = isSignInMode,
            onBackClick = onBackClick,
        )
    }
    composable<NotificationRoute> { backStackEntry ->
        val unreadMsgCount = backStackEntry.toRoute<NotificationRoute>().unreadMsgCount
        NotificationScreen(
            unreadMsgCount = unreadMsgCount,
            onBackClick = onBackClick,
            onMessageClick = onFeedClick
        )
    }
    composable<ReadRecordRoute> {
        ReadRecordScreen(
            onBackClick = onBackClick,
            onCollectionClick = onFeedClick
        )
    }
}

fun NavHostController.navigateToSignInOrUp(isSignIn: Boolean, navOptions: NavOptions? = null) {
    navigate(SignInOrUpRoute(isSignIn = isSignIn), navOptions)
}

fun NavHostController.navigateToNotification(unreadMsgCount: Int, navOptions: NavOptions? = null) {
    navigate(NotificationRoute(unreadMsgCount = unreadMsgCount), navOptions)
}