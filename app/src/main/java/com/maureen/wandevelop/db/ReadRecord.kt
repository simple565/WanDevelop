package com.maureen.wandevelop.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 稍后阅读
 * @author lianml
 * @date 2024/1/9
 */
@Entity(tableName = "read_record")
data class ReadRecord(
    @PrimaryKey
    val url: String,
    val title: String,
    val author: String,
    val category: String,
    val description: String,
    val read: Boolean
)
