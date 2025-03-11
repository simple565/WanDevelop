package com.maureen.wandevelop.common.entity

/**
 * @author lianml
 * @date 2025/3/12
 */
data class NodeInfo(
    val id: Long,
    val name: String,
    val expanded: Boolean = false,
    val expandable: Boolean = false
)
