package com.maureen.wandevelop.common

import android.util.Log
import com.maureen.wandevelop.base.BaseRepository
import com.maureen.wandevelop.db.AppDatabase
import com.maureen.wandevelop.db.ReadRecord
import com.maureen.wandevelop.common.entity.Feed
import com.maureen.wandevelop.network.WanAndroidService

/**
 * @author lianml
 * @date 2025/1/13
 */
open class FeedRepository : BaseRepository() {
    companion object {
        private const val TAG = "FeedRepository"
    }

    fun loadCollected() = AppDatabase.instance.readRecordDao().queryRecordList(ReadRecord.TYPE_COLLECT)

    suspend fun addReadRecord(feed: Feed, type: Int = ReadRecord.TYPE_READ_LATER) {
        val record = ReadRecord(
            id = feed.id,
            url = feed.url,
            title = feed.title,
            author = feed.author,
            tags = feed.tags.joinToString { it.name },
            timestamp = System.currentTimeMillis(),
            type = type
        )
        AppDatabase.instance.readRecordDao().addOrUpdateRecord(record)
    }

    suspend fun toggleCollect(feed: Feed, collect: Boolean): Pair<Boolean, String> {
        val result = if (collect) {
            WanAndroidService.instance.collect(feed.id)
        } else {
            WanAndroidService.instance.cancelCollect(feed.id)
        }
        Log.d(TAG, "collectOrNot: $collect article $feed.id result ${result.errorCode}")
        return Pair(result.isSuccess, if (result.isSuccess) "" else result.errorMsg)
    }
}