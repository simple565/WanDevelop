package com.maureen.wandevelop.feature.home

import androidx.paging.Pager
import com.maureen.wandevelop.core.ext.newWanAndroidPager
import com.maureen.wandevelop.core.network.NetworkRequest
import com.maureen.wandevelop.network.WanAndroidService
import com.maureen.wandevelop.network.entity.ArticleInfo

/**
 * Function: 首页相关
 * @author lianml
 * Create 2021-02-17
 */
class HomePageRepository {

    /**
     * 加载banner
     */
    suspend fun loadBannerData() = NetworkRequest.requestSafely { WanAndroidService.instance.banner() }

    private suspend fun loadStickyArticleList(): List<ArticleInfo> {
        return WanAndroidService.instance.stickyArticleList()
                .takeIf { it.isSuccessWithData }
                ?.run { this.data }
                ?: emptyList()
    }

    /**
     * 加载首页文章列表
     */
    fun loadHomeArticleList(showStickTop: Boolean = true): Pager<Int, ArticleInfo> {
        return newWanAndroidPager(
            preLoadDataBlock = { pageNum -> if (showStickTop && pageNum == 0) this::loadStickyArticleList.invoke() else emptyList() },
            loadDataBlock = { pageNum -> WanAndroidService.instance.homeArticleList(pageNum) }
        )
    }
}