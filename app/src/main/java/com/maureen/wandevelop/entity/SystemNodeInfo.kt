package com.maureen.wandevelop.entity

import com.squareup.moshi.Json

/**
 * 体系节点实体类
 * @author lianml
 * @date 2025/2/18
 */
data class SystemNodeInfo(
    val articleList: List<ArticleInfo>,
    val author: String,
    val children: List<SystemNodeInfo>,
    val courseId: Int,
    val cover: String,
    val desc: String,
    val id: Long,
    @Json(name = "lisense")
    val license: String,
    @Json(name = "lisenseLink")
    val licenseLink: String,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val type: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)