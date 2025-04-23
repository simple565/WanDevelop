package com.maureen.wandevelop.feature.discovery

import androidx.paging.PagingData
import com.maureen.wandevelop.base.BaseRepository
import com.maureen.wandevelop.entity.ArticleInfo
import com.maureen.wandevelop.ext.newWanAndroidPager
import com.maureen.wandevelop.network.WanAndroidService
import kotlinx.coroutines.flow.Flow

/**
 * @author lianml
 * @date 2025/2/9
 */
class DiscoveryRepository : BaseRepository() {
    companion object {
        private const val TAG = "DiscoveryRepository"
        private val routeNameRegex = Regex(".* - 学习路径|Android 性能优化-长期分享-BaguTree组织")
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
    suspend fun getCourseList() = requestSafely { WanAndroidService.instance.courseList() }

    /**
     * 体系一级分类列表过滤出学习路线
     */
    suspend fun getRouteList() = requestSafely { WanAndroidService.instance.treeList() }.let {
        if (it.isSuccessWithData) {
            return@let it.copy(data = it.data?.filter { nodeInfo ->
                nodeInfo.name.matches(
                    routeNameRegex
                )
            })
        }
        return@let it
    }
}