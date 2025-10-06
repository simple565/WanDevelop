package com.maureen.wandevelop.network.entity

import kotlinx.serialization.Serializable

/**
 * @author lianml
 * @date 2024/12/29
 */
@Serializable
data class ProfileInfo(
    val name: String = "",
    val email: String = "",
    val rank: String = "",
    val coin: String = "",
    val level: String = "",
    val unreadMsgCount: Int = 0
)

val UserInfo.userName: String
    get() {
        val builder = StringBuilder(this.username)
        if (this.publicName.isNotEmpty()) {
            builder.append("(").append(this.publicName).append(")")
        }
        return builder.toString()
    }

fun UserInfo.toProfileInfo(): ProfileInfo {
    return ProfileInfo(
        name = this.userName,
        email = this.email,
        coin = this.coinCount.toString()
    )
}

fun UserDetailInfo.toProfileInfo(): ProfileInfo {
    return this.userInfo.toProfileInfo().copy(
        rank = this.coinInfo.rank,
        level = this.coinInfo.level.toString()
    )
}
