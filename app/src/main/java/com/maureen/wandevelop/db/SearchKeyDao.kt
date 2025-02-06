package com.maureen.wandevelop.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * @author lianml
 * @date 2025/2/3
 */
@Dao
interface SearchKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchKey(vararg key: SearchKey)

    @Query("SELECT * FROM search_key WHERE type = 0")
    fun querySearchRecentKey(): Flow<List<SearchKey>>

    @Query("DELETE FROM search_key WHERE type = 0")
    suspend fun clearSearchRecentKey()

    @Query("DELETE FROM search_key WHERE `key` IN (:keys)")
    suspend fun deleteSearchRecentKey(keys: List<String>)

    @Query("SELECT * FROM search_key WHERE type = 1")
    fun queryHotkey(): Flow<List<SearchKey>>

    @Query("DELETE FROM search_key WHERE type = 1")
    suspend fun clearHotkey()
}