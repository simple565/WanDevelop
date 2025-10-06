package com.maureen.wandevelop.core.entity

import com.maureen.wandevelop.network.entity.TagInfo
import kotlinx.serialization.Serializable

/**
 * 通用信息流实体类
 * @date 2025/2/7
 */
@Serializable
data class Feed (
    val id: Int,
    val title: String,
    val url: String = "",
    val coverUrl: String = "",
    val isCollect: Boolean = false,
    val author: String = "",
    val publishData: String = "",
    val description: String = "",
    val tags: List<TagInfo> = emptyList(),
)

