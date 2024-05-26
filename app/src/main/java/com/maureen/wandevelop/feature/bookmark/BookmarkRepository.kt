package com.maureen.wandevelop.feature.bookmark

import com.maureen.wandevelop.base.BaseRepository
import com.maureen.wandevelop.db.AppDatabase
import com.maureen.wandevelop.db.ReadRecord
import com.maureen.wandevelop.network.WanAndroidService

/**
 * 阅读记录相关
 * @author lianml
 * @date 2024-02-18
 */
class BookmarkRepository : BaseRepository() {

    suspend fun loadReadLater(): List<ReadRecord> {
        return AppDatabase.instance.readLaterDao().queryReadLaterList()
    }

    suspend fun loadBookmark(page: Int) = request { WanAndroidService.instance.bookmarkList(page) }

    suspend fun cancelBookmark(id: Long) = request { WanAndroidService.instance.cancelMyBookmark(id) }

}