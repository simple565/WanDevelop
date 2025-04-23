package com.maureen.wandevelop.common.entity

/**
 * 可展开节点实体类
 * @date 2025/3/12
 */
data class NodeInfo(
    val id: Long,
    val name: String,
    val expanded: Boolean = false,
    val expandable: Boolean = false
)
