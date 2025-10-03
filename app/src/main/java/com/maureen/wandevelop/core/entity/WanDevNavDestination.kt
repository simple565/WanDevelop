package com.maureen.wandevelop.core.entity

/**
 * 导航
 * @date 2025/3/31
 */
data class WanDevNavDestination<T : Any>(
    val iconId: Int,
    val labelId: Int,
    val route: T
)