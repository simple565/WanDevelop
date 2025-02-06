package com.maureen.wandevelop.db

import androidx.room.Entity

/**
 * 搜索词
 * @author lianml
 * @date 2025/2/3
 */
@Entity(tableName = "search_key", primaryKeys = ["key", "type"])
data class SearchKey(
    val key: String,
    val type: Int,
    val timestamp: Long,
) {
  companion object {
      const val RECENT = 0
      const val HOTKEY = 1
  }
}