package com.maureen.wandevelop.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * @author lianml
 * @date 2024/1/9
 */
@Dao
interface ReadRecordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addOrIgnoreReadRecord(record: ReadRecord)
    @Query("SELECT * FROM read_record")
    fun queryRecordList(): List<ReadRecord>
    @Query("SELECT * FROM read_record WHERE read = 0")
    fun queryReadLaterList(): List<ReadRecord>
    @Delete
    fun deleteRecord(record: ReadRecord)
    @Query("DELETE FROM read_record")
    fun clearReadLater()
}