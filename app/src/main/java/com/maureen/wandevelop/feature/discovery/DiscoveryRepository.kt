package com.maureen.wandevelop.feature.discovery

import androidx.paging.PagingData
import com.maureen.wandevelop.core.ext.newWanAndroidPager
import com.maureen.wandevelop.core.network.NetworkRequest
import com.maureen.wandevelop.network.WanAndroidService
import com.maureen.wandevelop.network.entity.ArticleInfo
import kotlinx.coroutines.flow.Flow

/**
 * @author lianml
 * @date 2025/2/9
 */
class DiscoveryRepository {
    companion object {
        private const val TAG = "DiscoveryRepository"
    }

    fun getSquareArticleFlow(): Flow<PagingData<ArticleInfo>> {
        return newWanAndroidPager(
            loadDataBlock = { pageNum ->
                WanAndroidService.instance.squareArticleList(pageNum)
            }
        ).flow
    }

    fun getQaFlow(): Flow<PagingData<ArticleInfo>> {
        return newWanAndroidPager(
            startPage = WanAndroidService.DEFAULT_START_PAGE_INDEX_ALIAS,
            loadDataBlock = { pageNum ->
                WanAndroidService.instance.qaList(pageNum)
            }
        ).flow
    }

    /**
     * 课程列表
     */
    suspend fun getCourseList() = NetworkRequest.requestSafely { WanAndroidService.instance.courseList() }

    /**
     * 体系列表
     */
    suspend fun getSystemNodeList() = NetworkRequest.requestSafely { WanAndroidService.instance.systemNodeList() }

    /**
     * 体系节点下文章列表
     */
    suspend fun getSystemNodeArticleList(cid: Int) = NetworkRequest.requestSafely { WanAndroidService.instance.systemNodeArticleList(0, cid) }
}