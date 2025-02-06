package com.maureen.wandevelop.db

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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdateRecord(record: ReadRecord)

    @Query("SELECT * FROM read_record WHERE read = 1")
    fun queryRecordList(): Flow<List<ReadRecord>>

    @Query("SELECT * FROM read_record WHERE read = 0")
    fun queryReadLaterList(): Flow<List<ReadRecord>>

    @Query("DELETE FROM read_record")
    fun clearAll()
}