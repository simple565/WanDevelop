package com.maureen.wandevelop.entity

import kotlinx.serialization.SerialName

/**
 * @author lianml
 * @date 2025/2/9
 */
data class CourseInfo(
    val articleList: List<Any>,
    val author: String,
    val children: List<Any>,
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