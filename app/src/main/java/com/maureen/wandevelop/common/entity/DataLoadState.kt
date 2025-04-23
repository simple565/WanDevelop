package com.maureen.wandevelop.common.entity

/**
 * 数据加载状态
 * @date 2025/3/9
 */
data class DataLoadState<T>(
    val isLoading: Boolean = false,
    val errorMsg: String = "",
    val dataList: List<T> = emptyList(),
    val operatedIdSet: Set<Long> = emptySet()
)