package com.maureen.wandevelop.entity

import kotlinx.serialization.Serializable

/**
 * 收藏的文章
 * @author lianml
 * @date 2024/2/17
 */
@Serializable
data class CollectionInfo(
    val author: String,
    val chapterId: Int,
    val chapterName: String,
    val courseId: Int,
    val desc: String,
    val envelopePic: String,
    val id: Long,
    val link: String,
    val niceDate: String,
    val origin: String,
    val originId: Long,
    val publishTime: Long,
    val title: String,
    val userId: Int,
    val visible: Int,
    val zan: Int
)