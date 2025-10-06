package com.maureen.wandevelop.feature.profile.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.maureen.wandevelop.R
import com.maureen.wandevelop.core.entity.Feed
import com.maureen.wandevelop.db.ReadRecord
import com.maureen.wandevelop.network.entity.CollectionInfo
import com.maureen.wandevelop.network.entity.TagInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.DateFormat

class BookmarkViewModel() : ViewModel() {
    companion object {
        private const val TAG = "BookmarkViewModel"
        val bookmarkNames = listOf(R.string.prompt_my_collection, R.string.prompt_read_later)
    }

    private val repository = BookmarkRepository()
    private val bookmarkFlowMap: MutableMap<Int, Flow<PagingData<Feed>>> = mutableMapOf()

    @OptIn(ExperimentalCoroutinesApi::class)
    val collectionFlow = repository.loadCollectedIdSet().flatMapLatest { idSet ->
        repository.loadCollectionList().flow.map { pagingData ->
            pagingData.filter { it.originId in idSet }.map { it.toFeed() }
        }
    }.cachedIn(viewModelScope)

    fun getReadRecordFlow(recordType: Int): Flow<PagingData<Feed>> {
        return bookmarkFlowMap[recordType]
            ?: repository.loadReadRecord(recordType).flow.map { pagingData ->
                pagingData.map { it.toFeed() }
            }.cachedIn(viewModelScope).also { bookmarkFlowMap[recordType] = it }
    }

    fun cancelCollect(id: Int) = viewModelScope.launch(Dispatchers.Default) {
        repository.toggleCollect(id, false)
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
            id = this.originId,
            title = this.title,
            url = this.link,
            isCollect = true,
            author = this.author,
            publishData = this.niceDate,
            tags = listOf(TagInfo(name = this.chapterName, url = ""))
        )
    }
}