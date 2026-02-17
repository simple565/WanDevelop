package com.maureen.wandevelop.network.entity

import kotlinx.serialization.Serializable

/**
 * 专栏实体类
 * @author Lian
 * @date 2026/2/16
 */
@Serializable
data class ColumnInfo(
    val chapterId: Int,
    val chapterName: String,
    val columnId: Int,
    val id: Int,
    val name: String,
    val subChapterId: Int,
    val subChapterName: String,
    val url: String,
    val userId: Int
)