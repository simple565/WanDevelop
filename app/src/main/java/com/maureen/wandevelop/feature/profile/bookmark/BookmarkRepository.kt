package com.maureen.wandevelop.feature.profile.bookmark

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.maureen.wandevelop.core.ext.newWanAndroidPager
import com.maureen.wandevelop.core.feed.FeedRepository
import com.maureen.wandevelop.db.AppDatabase
import com.maureen.wandevelop.db.ReadRecord
import com.maureen.wandevelop.network.WanAndroidService

/**
 * 书签相关代码
 * @date 2024-02-18
 */
class BookmarkRepository : FeedRepository() {

    fun loadCollectionList() = newWanAndroidPager { pageNum -> WanAndroidService.instance.collectionList(pageNum) }

    fun loadReadRecord(recordType: Int = ReadRecord.TYPE_READ_LATER): Pager<Int, ReadRecord> {
        return Pager(
            config = PagingConfig(WanAndroidService.DEFAULT_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                AppDatabase.instance.readRecordDao().queryRecord(recordType)
            }
        )
    }

}