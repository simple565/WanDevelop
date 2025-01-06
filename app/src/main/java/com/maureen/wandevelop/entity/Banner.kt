package com.maureen.wandevelop.entity

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

/**
 * @author lianml
 * @date 2023/12/20
 */
@Serializable
data class Banner(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)