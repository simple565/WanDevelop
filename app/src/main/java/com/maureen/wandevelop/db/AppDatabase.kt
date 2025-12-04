package com.maureen.wandevelop.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maureen.wandevelop.MyApplication

/**
 * @author lianml
 * @date 2024/1/9
 */
@Database(entities = [ReadRecord::class, SearchKey::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        @JvmStatic
        val instance by lazy {
            Room.databaseBuilder(
                MyApplication.instance.applicationContext,
                AppDatabase::class.java, "database-name"
            ).build()
        }
    }

    abstract fun readRecordDao(): ReadRecordDao

    abstract fun searchKeyDao(): SearchKeyDao
}