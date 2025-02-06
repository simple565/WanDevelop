package com.maureen.wandevelop.entity

import kotlinx.serialization.Serializable

/**
 * 搜索热词
 * @author lianml
 * @date 2023/12/20
 */
@Serializable
data class Hotkey(
    val id: Int,
    val link: String = "",
    val name: String,
    val order: Int,
    val visible: Int = 1
)