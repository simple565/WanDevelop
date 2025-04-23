package com.maureen.wandevelop.feature.home

import androidx.paging.Pager
import com.maureen.wandevelop.base.BaseRepository
import com.maureen.wandevelop.entity.ArticleInfo
import com.maureen.wandevelop.ext.newWanAndroidPager
import com.maureen.wandevelop.network.WanAndroidService

/**
 * Function: 首页相关
 * @author lianml
 * Create 2021-02-17
 */
class HomePageRepository : BaseRepository() {

    /**
     * 加载banner
     */
    suspend fun loadBannerData() = requestSafely { WanAndroidService.instance.banner() }

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