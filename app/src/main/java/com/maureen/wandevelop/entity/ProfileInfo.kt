package com.maureen.wandevelop.entity

/**
 * @author lianml
 * @date 2024/12/29
 */

data class ProfileInfo(
    val userDetailInfo: UserDetailInfo? = null,
    val unreadCount: Int = 0,
    val darkModeState: String,
    val cacheSize: String
)
