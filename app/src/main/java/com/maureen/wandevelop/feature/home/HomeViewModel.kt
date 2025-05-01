package com.maureen.wandevelop.feature.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.maureen.wandevelop.MyApplication
import com.maureen.wandevelop.core.entity.DataLoadState
import com.maureen.wandevelop.core.feed.FeedViewModel
import com.maureen.wandevelop.ext.toFeed
import com.maureen.wandevelop.util.UserPrefUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel() : FeedViewModel() {
    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val repository = HomePageRepository()

    val bannerListState =
        UserPrefUtil.getPreferenceFlow(UserPrefUtil.KEY_SHOW_BANNER, true.toString())
            .map {
                if (it == false.toString()) {
                    return@map Pair(false, DataLoadState())
                }
                val response = repository.loadBannerData()
                Log.d(TAG, "load banner result: ${response.errorMsg}")
                val state = if (response.isSuccessWithData) {
                    DataLoadState(isLoading = false, dataList = response.data ?: emptyList())
                } else {
                    DataLoadState(isLoading = false, errorMsg = response.errorMsg)
                }
                return@map Pair(true, state)
            }
            .flowOn(Dispatchers.IO).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = Pair(true, DataLoadState(isLoading = true))
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    val feedListFlow = UserPrefUtil.getPreferenceFlow(UserPrefUtil.KEY_SHOW_STICK_TOP_ARTICLE, true.toString())
            .flatMapLatest {
                val context = MyApplication.instance.applicationContext
                repository.loadHomeArticleList(it?.toBoolean() == true).flow.map { pagingData ->
                    pagingData.map { it.toFeed(context) }
                }
            }.cachedIn(viewModelScope)


}