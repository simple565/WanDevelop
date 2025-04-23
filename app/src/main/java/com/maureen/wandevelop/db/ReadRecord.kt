package com.maureen.wandevelop.db

import androidx.room.Entity

/**
 * 阅读记录包含稍后阅读表
 * @author lianml
 * @date 2024/1/9
 */
@Entity(tableName = "read_record", primaryKeys = ["id", "type"])
data class ReadRecord(
    val id: Long,
    val url: String,
    val title: String,
    val author: String,
    val tags: String,
    val timestamp: Long,
    val type: Int
) {
    companion object {
        const val TYPE_READ_LATER = 1
        const val TYPE_READ_HISTORY = 2
        const val TYPE_COLLECT = 3
    }
}
