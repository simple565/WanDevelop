package com.maureen.wandevelop.entity

/**
 * 站内消息实体类
 * @author lianml
 * @date 2024/5/4
 */
data class MessageInfo(
    val category: Int,
    val date: Long,
    val fromUser: String,
    val fromUserId: Int,
    val fullLink: String,
    val id: Int,
    val isRead: Int,
    val link: String,
    val message: String,
    val niceDate: String,
    val tag: String,
    val title: String,
    val userId: Int
)