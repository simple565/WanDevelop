package com.maureen.wandevelop.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 阅读记录包含稍后阅读表
 * @author lianml
 * @date 2024/1/9
 */
@Entity(tableName = "read_record")
data class ReadRecord(
    @PrimaryKey
    val id: Long,
    val url: String,
    val title: String,
    val author: String,
    val tags: String,
    val description: String,
    val read: Boolean
)
