package com.maureen.wandevelop.common.entity

/**
 * 操作状态
 * @date 2025/3/16
 */
data class OperationState(
    val isOperating: Boolean = false,
    val operatingMsg: String = "",
    val result: Boolean = true,
    val resultMsg: String = ""
)