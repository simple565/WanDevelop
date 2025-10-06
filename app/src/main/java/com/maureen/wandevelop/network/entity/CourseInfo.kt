package com.maureen.wandevelop.network.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author lianml
 * @date 2025/2/9
 */
@Serializable
data class CourseInfo(
    val articleList: List<CourseInfo>,
    val author: String,
    val children: List<CourseInfo>,
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