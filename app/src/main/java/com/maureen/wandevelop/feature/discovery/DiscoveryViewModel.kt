package com.maureen.wandevelop.feature.discovery

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.maureen.wandevelop.MyApplication
import com.maureen.wandevelop.R
import com.maureen.wandevelop.core.entity.DataLoadState
import com.maureen.wandevelop.core.entity.Feed
import com.maureen.wandevelop.core.feed.FeedViewModel
import com.maureen.wandevelop.ext.toFeed
import com.maureen.wandevelop.network.entity.SystemNodeInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

        /**
         * 体系数据中根据名称过滤出展示学习路径
         */
        private val routeNameRegex = Regex(".* - 学习路径|Android 性能优化-长期分享-BaguTree组织")
    }

    val pageList = DiscoveryPage.entries

    private val discoveryRepository: DiscoveryRepository = DiscoveryRepository()

    private val pagingDataFlowMap = mutableMapOf<DiscoveryPage, Flow<PagingData<Feed>>>()

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

    val loadCourseListState: StateFlow<DataLoadState<SystemNodeInfo>> = combine(
        flow { emit(discoveryRepository.getCourseList()) }.map { DataLoadState(dataList = it.data ?: emptyList(), errorMsg = it.errorMsg) },
        flow { emit(discoveryRepository.getSystemNodeList()) }.map {
            DataLoadState(
                dataList = it.data?.filter { systemNode -> systemNode.name.matches(routeNameRegex) } ?: emptyList(),
                errorMsg = it.errorMsg
            )
        }
    ) { courseResult, systemResult ->
        DataLoadState(
            isLoading = false,
            dataList = courseResult.dataList + systemResult.dataList,
            errorMsg = courseResult.errorMsg.ifBlank { systemResult.errorMsg }
        )
    }.flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = DataLoadState(isLoading = true)
        )

    fun toggleFeedCollectState(feed: Feed) = viewModelScope.launch(Dispatchers.IO) {
        // TODO: 校验是否登录
        feedRepository.toggleCollect(feed.id, feed.isCollect.not())
    }
}

enum class DiscoveryPage(val titleRes: Int, val isPageData: Boolean) {
    SQUARE(R.string.nav_square, true),
    QA(R.string.nav_qa, true),
    COURSE(R.string.nav_course, false)
}

