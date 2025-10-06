package com.maureen.wandevelop.core.entity

/**
 * 可展开节点实体类
 * @date 2025/3/12
 */
data class NodeInfo(
    val id: Int,
    val name: String,
    val expanded: Boolean = false,
    val feedList: List<Feed> = emptyList()
)