package com.maureen.wandevelop.common.feed

import android.util.Log
import com.maureen.wandevelop.base.BaseRepository
import com.maureen.wandevelop.common.entity.Feed
import com.maureen.wandevelop.db.AppDatabase
import com.maureen.wandevelop.db.ReadRecord
import com.maureen.wandevelop.network.WanAndroidService
import com.maureen.wandevelop.util.UserPrefUtil
import kotlinx.coroutines.flow.map

/**
 * @author lianml
 * @date 2025/1/13
 */
open class FeedRepository : BaseRepository() {
    companion object {
        private const val TAG = "FeedRepository"
    }

    fun loadCollectedIdSet() = UserPrefUtil.getPreferenceFlow(UserPrefUtil.KEY_USER_COLLECT_ID).map {
        return@map if (it.isNullOrBlank()) {
            emptySet<Long>()
        } else {
            it.split(",").map { it.toLong() }.toSet()
        }
    }

    suspend fun addReadRecord(feed: Feed, type: Int = ReadRecord.TYPE_READ_LATER) {
        val privateMode = UserPrefUtil.getPreference(UserPrefUtil.KEY_PRIVATE_MODE)?.toBooleanStrictOrNull() == true
        if (privateMode) {
            // 无痕模式不保存浏览记录
            return
        }
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
        val curCollectIsSet = UserPrefUtil.getPreference(UserPrefUtil.KEY_USER_COLLECT_ID)?.let {
            it.split(",").map { id -> id.toLong() }
        }?.toMutableSet() ?: mutableSetOf<Long>()
        Log.d(TAG, "toggleCollect: collect id set count before ${curCollectIsSet.size}")
        val result = if (collect) {
            WanAndroidService.instance.collect(feed.id)
        } else {
            WanAndroidService.instance.cancelCollect(feed.id)
        }
        Log.d(TAG, "collectOrNot: $collect article $feed.id result ${result.errorCode}")

        if (result.isSuccess) {
            if (collect) {
                curCollectIsSet.add(feed.id)
            } else {
                curCollectIsSet.remove(feed.id)
            }
            Log.d(TAG, "toggleCollect: collect id set count after ${curCollectIsSet.size}")
            UserPrefUtil.setPreference(UserPrefUtil.KEY_USER_COLLECT_ID, curCollectIsSet.joinToString(","))
        }
        return Pair(result.isSuccess, if (result.isSuccess) "" else result.errorMsg)
    }
}