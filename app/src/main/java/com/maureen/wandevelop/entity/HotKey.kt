package com.maureen.wandevelop.entity

import androidx.annotation.Keep

/**
 * 搜索热词
 * @author lianml
 * @date 2023/12/20
 */
@Keep
data class HotKey(
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int
)