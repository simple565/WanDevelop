package com.maureen.wandevelop.entity

/**
 * 收藏文章
 * @author lianml
 * @date 2024/2/17
 */
data class Bookmark(
    val author: String = "",
    val chapterId: Int = -1,
    val chapterName: String = "",
    val courseId: Int = -1,
    val desc: String = "",
    val envelopePic: String = "",
    val id: Int = -1,
    val link: String = "",
    val niceDate: String = "",
    val origin: String = "",
    val originId: Int = -1,
    val publishTime: Long = -1L,
    val title: String = "",
    val userId: Int = -1,
    val visible: Int = -1,
    val zan: Int = -1
)