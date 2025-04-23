package com.maureen.wandevelop.feature.setting

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object AboutRoute

@Serializable
object SettingRoute

fun NavGraphBuilder.settingScreens(
    onBackClick: () -> Unit
) {
    composable<AboutRoute> {
        AboutScreen(
            onBackClick = onBackClick
        )
    }
    composable<SettingRoute> {
        SettingsScreen(
            onBackClick = onBackClick
        )
    }
}

