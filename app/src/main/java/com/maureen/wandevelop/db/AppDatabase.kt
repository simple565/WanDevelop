package com.maureen.wandevelop.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maureen.wandevelop.MyApplication

/**
 * @author lianml
 * @date 2024/1/9
 */
@Database(entities = [ReadRecord::class, SearchRecent::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        val instance by lazy {
            Room.databaseBuilder(
                MyApplication.instance.applicationContext,
                AppDatabase::class.java, "database-name"
            ).build()
        }
    }
    abstract fun readLaterDao(): ReadRecordDao

    abstract fun searchRecentDao(): SearchRecentDao
}