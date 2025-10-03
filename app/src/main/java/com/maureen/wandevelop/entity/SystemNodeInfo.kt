package com.maureen.wandevelop.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 体系节点实体类
 * @author lianml
 * @date 2025/2/18
 */
@Serializable
data class SystemNodeInfo(
    val articleList: List<ArticleInfo>,
    val author: String,
    val children: List<SystemNodeInfo>,
    val courseId: Int,
    val cover: String,
    val desc: String,
    val id: Long,
    @SerialName("lisense")
    val license: String,
    @SerialName("lisenseLink")
    val licenseLink: String,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val type: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)