package com.maureen.wandevelop.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * @author lianml
 * @date 2024/1/9
 */
@Dao
interface SearchRecentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addOrIgnoreSearchRecent(record: SearchRecent)

    @Query("SELECT * FROM search_recent")
    fun querySearchRecentList(): List<SearchRecent>

    @Query("DELETE FROM search_recent")
    fun clearReadRecent()
}