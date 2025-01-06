package com.maureen.wandevelop.entity

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

/**
 * 用户信息
 * @author lianml
 * @date 2023/12/19
 */
@Serializable
data class UserDetailInfo(
    val coinInfo: CoinInfo,
    val collectArticleInfo: CollectArticleInfo,
    val userInfo: UserInfo
)

@Serializable
data class UserInfo(
    val admin: Boolean,
    val chapterTops: List<Int>,
    val coinCount: Int,
    val collectIds: List<Int>,
    val email: String,
    val icon: String,
    val id: Int,
    val nickname: String,
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String
) {
    val userName: String
        get() {
            val builder = StringBuilder(this.username)
            if (this.publicName.isNotEmpty()) {
                builder.append("(").append(this.publicName).append(")")
            }
            return builder.toString()
        }
}

/**
 * 用户登记、积分及排名
 */
@Serializable
data class CoinInfo(
    val coinCount: Int,
    val level: Int,
    val nickname: String,
    val rank: String,
    val userId: Int,
    val username: String
)

@Serializable
data class CollectArticleInfo(
    val count: Int
)

/**
 * 分享者积分信息及分享文章列表
 */
@Serializable
data class SharerInfo(
    val coinInfo: CoinInfo,
    val shareList: BasePage<Article>
)