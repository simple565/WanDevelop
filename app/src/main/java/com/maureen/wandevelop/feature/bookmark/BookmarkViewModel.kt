package com.maureen.wandevelop.feature.bookmark

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.maureen.wandevelop.base.getCommonPager
import com.maureen.wandevelop.entity.Bookmark
import com.maureen.wandevelop.network.WanAndroidService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class BookmarkViewModel : ViewModel() {
    companion object {
        private const val TAG = "BookmarkViewModel"
    }

    private val repository = BookmarkRepository()
    private var bookmarkFlow: Flow<PagingData<Bookmark>>? = null

    fun loadReadLater() = viewModelScope.launch(Dispatchers.IO) {
        repository.loadReadLater().map {
            Bookmark(
                title = it.title,
                link = it.url,
                desc = it.description,
                author = it.author,
                chapterName = it.category
            )
        }.also {
            Log.d(TAG, "loadReadLater: ${it.size}")
        }
    }

    fun loadBookmark(): Flow<PagingData<Bookmark>> {
        return bookmarkFlow ?: getCommonPager { pageNum ->
            WanAndroidService.instance.bookmarkList(
                pageNum
            )
        }.flow
            .flowOn(Dispatchers.IO)
            .cachedIn(viewModelScope)
            .also { bookmarkFlow = it }
    }

    fun deleteBookMark() = viewModelScope.launch(Dispatchers.IO) {

    }
}