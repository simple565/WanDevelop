package com.maureen.wandevelop.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 最近搜索
 * @author lianml
 * @date 2024/1/9
 */
@Entity(tableName = "search_recent")
data class SearchRecent(
    @PrimaryKey
    val keyword: String
)
