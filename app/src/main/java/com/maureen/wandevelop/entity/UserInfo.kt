package com.maureen.wandevelop.entity

import androidx.annotation.Keep

/**
 * 用户信息
 * @author lianml
 * @date 2023/12/19
 */
@Keep
data class UserDetailInfo(
    val coinInfo: CoinInfo,
    val collectArticleInfo: CollectArticleInfo,
    val userInfo: UserInfo
)

@Keep
data class UserInfo(
    val admin: Boolean,
    val chapterTops: List<Any>,
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
)

/**
 * 用户登记、积分及排名
 */
@Keep
data class CoinInfo(
    val coinCount: Int,
    val level: Int,
    val nickname: String,
    val rank: String,
    val userId: Int,
    val username: String
)

@Keep
data class CollectArticleInfo(
    val count: Int
)