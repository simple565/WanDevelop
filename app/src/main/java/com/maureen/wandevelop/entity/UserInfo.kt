package com.maureen.wandevelop.entity

import kotlinx.serialization.Serializable

/**
 * 用户详细信息, 获取用户信息接口请求返回
 * @author lianml
 * @date 2023/12/19
 */
@Serializable
data class UserDetailInfo(
    val coinInfo: CoinInfo,
    val collectArticleInfo: CollectArticleInfo,
    val userInfo: UserInfo
)

/**
 * 用户信息，登录成功后返回
 */
@Serializable
data class UserInfo(
    val admin: Boolean,
    val chapterTops: List<Long>,
    val coinCount: Int,
    val collectIds: List<Long>,
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
 * 用户等级、积分及排名
 */
@Serializable
data class CoinInfo(
    val coinCount: Long = 0L,
    val level: Int = 0,
    val nickname: String = "",
    val rank: String = "",
    val userId: Int = 0,
    val username: String = ""
)

@Serializable
data class CollectArticleInfo(
    val count: Int = 0
)

/**
 * 分享者积分信息及分享文章列表
 */
@Serializable
data class SharerInfo(
    val coinInfo: CoinInfo,
    val shareList: BasePage<ArticleInfo>
)