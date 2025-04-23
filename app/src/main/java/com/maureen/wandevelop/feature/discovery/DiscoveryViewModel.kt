package com.maureen.wandevelop.feature.discovery

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.maureen.wandevelop.MyApplication
import com.maureen.wandevelop.R
import com.maureen.wandevelop.common.entity.DataLoadState
import com.maureen.wandevelop.common.entity.Feed
import com.maureen.wandevelop.common.feed.FeedViewModel
import com.maureen.wandevelop.entity.SystemNodeInfo
import com.maureen.wandevelop.ext.toFeed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DiscoveryViewModel : FeedViewModel() {
    companion object {
        private const val TAG = "DiscoveryViewModel"
    }

    val pageList = DiscoveryPage.entries

    private val discoveryRepository: DiscoveryRepository = DiscoveryRepository()

    private val pagingDataFlowMap = mutableMapOf<DiscoveryPage, Flow<PagingData<Feed>>>()
    private val _expandedRouteIdSetState: MutableStateFlow<Set<Long>> = MutableStateFlow(emptySet())

    fun getPagingFlow(page: DiscoveryPage, useCache: Boolean = true): Flow<PagingData<Feed>> {
        val context = MyApplication.instance.applicationContext
        if (!page.isPageData) {
            return flowOf(PagingData.empty())
        }

        if (useCache && pagingDataFlowMap.contains(page)) {
            return pagingDataFlowMap[page]!!
        }
        val flow = when (page) {
            DiscoveryPage.SQUARE, DiscoveryPage.QA -> {
                if (page == DiscoveryPage.SQUARE) {
                    discoveryRepository.getSquareArticleFlow()
                } else {
                    discoveryRepository.getQaFlow()
                }.map { pagingData ->
                    pagingData.map { it.toFeed(context) }
                }
            }

            else -> {
                flowOf(PagingData.empty())
            }
        }.flowOn(Dispatchers.IO)
            .cachedIn(viewModelScope)
        pagingDataFlowMap[page] = flow
        return flow
    }

    val loadCourseListState: StateFlow<DataLoadState<Feed>> = flow {
        val result = discoveryRepository.getCourseList()
        Log.d(TAG, "load course count: ${result.data?.size}")
        emit(
            DataLoadState(
                isLoading = false,
                dataList = result.data?.map { it.toFeed() } ?: emptyList(),
                errorMsg = result.errorMsg
            ))
    }.flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = DataLoadState(isLoading = true)
        )

    val loadRouteListState: StateFlow<DataLoadState<SystemNodeInfo>> = combine(
        _expandedRouteIdSetState, flow { emit(discoveryRepository.getRouteList()) }
    ) { set, result ->
        DataLoadState(
            isLoading = false,
            dataList = result.data ?: emptyList(),
            errorMsg = result.errorMsg,
            operatedIdSet = set
        )
    }.flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = DataLoadState(isLoading = true)
        )

    fun toggleFeedCollectState(feed: Feed) = viewModelScope.launch(Dispatchers.IO) {
        // TODO: 校验是否登录
        feedRepository.toggleCollect(feed, feed.isCollect.not())
    }

    fun toggleRouteExpandState(nodeInfo: SystemNodeInfo) {
        val old = _expandedRouteIdSetState.value.toMutableSet()
        if (old.contains(nodeInfo.id)) {
            old.remove(nodeInfo.id)
        } else {
            old.add(nodeInfo.id)
        }
        _expandedRouteIdSetState.value = old
    }
}

enum class DiscoveryPage(val titleRes: Int, val isPageData: Boolean) {
    SQUARE(R.string.nav_square, true),
    QA(R.string.nav_qa, true),
    COURSE(R.string.nav_course, false),
    ROUTE(R.string.nav_learning_path, false)
}

