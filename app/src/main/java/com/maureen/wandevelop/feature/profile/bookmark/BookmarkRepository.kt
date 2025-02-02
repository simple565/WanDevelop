package com.maureen.wandevelop.feature.profile.bookmark

import com.maureen.wandevelop.base.BaseRepository
import com.maureen.wandevelop.base.WanAndroidPagePagingSource
import com.maureen.wandevelop.db.AppDatabase
import com.maureen.wandevelop.ext.toPager
import com.maureen.wandevelop.network.WanAndroidService

/**
 * 阅读记录相关
 * @author lianml
 * @date 2024-02-18
 */
class BookmarkRepository : BaseRepository() {

    fun loadReadLater() = AppDatabase.instance.readLaterDao().queryReadLaterList()

    fun loadCollectionList() = WanAndroidPagePagingSource { pageNum -> WanAndroidService.instance.collectionList(pageNum) }.toPager()

    suspend fun cancelBookmark(id: Long, originId: Long) = request { WanAndroidService.instance.cancelCollect(id, originId) }
}