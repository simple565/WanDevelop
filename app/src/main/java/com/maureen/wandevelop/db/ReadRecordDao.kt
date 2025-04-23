package com.maureen.wandevelop.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * @author lianml
 * @date 2024/1/9
 */
@Dao
interface ReadRecordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addOrUpdateRecord(record: ReadRecord)

    @Query("SELECT * FROM read_record WHERE type = (:type)")
    fun queryRecordList(type: Int): Flow<List<ReadRecord>>

    @Query("SELECT * FROM read_record WHERE type = (:type)")
    fun queryRecord(type: Int): PagingSource<Int, ReadRecord>

    @Query("DELETE FROM read_record WHERE type = (:type)")
    suspend fun deleteByType(type: Int)

    @Query("DELETE FROM read_record")
    suspend fun clearAll()
}