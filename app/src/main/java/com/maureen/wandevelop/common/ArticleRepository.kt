package com.maureen.wandevelop.common

import android.util.Log
import com.maureen.wandevelop.base.BaseRepository
import com.maureen.wandevelop.network.WanAndroidService

/**
 * @author lianml
 * @date 2025/1/13
 */
class ArticleRepository : BaseRepository() {
    companion object {
        private const val TAG = "ArticleRepository"
    }

    // suspend fun addReadLater() = AppDatabase.instance.readLaterDao().addOrUpdateRecord()

    suspend fun collectOrNot(id: Long, collect: Boolean): Pair<Boolean, String> {
        val result = if (collect) {
            WanAndroidService.instance.collect(id)
        } else {
            WanAndroidService.instance.cancelCollect(id)
        }
        Log.d(TAG, "collectOrNot: $collect article $id result ${result.errorCode}")

        return Pair(result.isSuccess, if (result.isSuccess) "" else result.errorMsg)
    }
}