package com.maureen.wandevelop.core.entity

/**
 * 导航
 * @date 2025/3/31
 */
data class WanDevNavDestination<T : Any>(
    @androidx.annotation.DrawableRes val iconId: Int,
    @androidx.annotation.StringRes val labelId: Int,
    val route: T
)