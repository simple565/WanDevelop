package com.maureen.wandevelop.feature.profile.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.maureen.wandevelop.R
import com.maureen.wandevelop.core.entity.Feed
import com.maureen.wandevelop.db.ReadRecord
import com.maureen.wandevelop.entity.CollectionInfo
import com.maureen.wandevelop.entity.TagInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.sql.Date
import java.text.DateFormat

class BookmarkViewModel() : ViewModel() {
    companion object {
        private const val TAG = "BookmarkViewModel"
        val bookmarkNames = listOf(R.string.prompt_my_collection, R.string.prompt_read_later)
    }

    private val repository = BookmarkRepository()
    private val bookmarkFlowMap : Map<Int, Flow<PagingData<Feed>>> = mutableMapOf()

    val collectionFlow = repository.loadCollectionList().flow
        .map { pagingData ->
            pagingData.map { it.toFeed() }
        }
        .cachedIn(viewModelScope)

    private val readRecordFlow = repository.loadReadRecord().flow
        .map { pagingData ->
            pagingData.map { it.toFeed() }
        }
        .cachedIn(viewModelScope)

    fun getReadRecordFlow(recordType: Int): Flow<PagingData<Feed>> {
        return bookmarkFlowMap[recordType] ?: readRecordFlow
    }

    private fun ReadRecord.toFeed(): Feed {
        return Feed(
            id = this.id,
            title = this.title,
            url = this.url,
            isCollect = true,
            author = this.author,
            publishData = DateFormat.getDateInstance().format(Date(this.timestamp)),
            tags = this.tags.split(",").map { tag -> TagInfo(name = tag, url = "") }
        )
    }

    private fun CollectionInfo.toFeed(): Feed {
        return Feed(
            id = this.id,
            title = this.title,
            url = this.link,
            isCollect = true,
            author = this.author,
            publishData = this.niceDate,
            tags = listOf(TagInfo(name = this.chapterName, url = "" ))
        )
    }
}