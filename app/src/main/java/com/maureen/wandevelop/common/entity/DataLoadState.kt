package com.maureen.wandevelop.common.entity

/**
 * @author lianml
 * @date 2025/3/9
 */
data class DataLoadState<T>(
    val isLoading: Boolean = false,
    val errorMsg: String = "",
    val dataList: List<T> = emptyList(),
    val operatedIdSet: Set<Long> = emptySet()
)