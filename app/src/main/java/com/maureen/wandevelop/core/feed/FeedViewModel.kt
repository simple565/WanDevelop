package com.maureen.wandevelop.core.feed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maureen.wandevelop.core.entity.Feed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * @author lianml
 * @date 2025/4/20
 */
open class FeedViewModel : ViewModel() {
    companion object {
        private const val TAG = "FeedViewModel"
    }
    protected val feedRepository = FeedRepository()

    val collectedIsSetFlow = feedRepository.loadCollectedIdSet()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptySet()
        )

    fun toggleFeedCollect(feed: Feed, collect: Boolean) = viewModelScope.launch(Dispatchers.Default) {
        Log.d(TAG, "toggleFeedCollect: ${feed.id} $collect")
        feedRepository.toggleCollect(feed = feed, collect = collect)
    }
}