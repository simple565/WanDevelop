package com.maureen.wandevelop.feature.home

import androidx.paging.Pager
import com.maureen.wandevelop.base.BaseRepository
import com.maureen.wandevelop.base.WanAndroidPagePagingSource
import com.maureen.wandevelop.entity.Article
import com.maureen.wandevelop.ext.toPager
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
    suspend fun loadBannerData() = WanAndroidService.instance.banner()

    private suspend fun loadStickyArticleList(): List<Article> {
        return WanAndroidService.instance.stickyArticleList()
                .takeIf { it.isSuccessWithData }
                ?.run { this.data }
                ?: emptyList()
    }

    /**
     * 加载首页文章列表
     */
    fun loadHomeArticleList(): Pager<Int, Article> {
        val showStickTop = true
        return WanAndroidPagePagingSource (
            loadDataBlock = { pageNum -> WanAndroidService.instance.homeArticleList(pageNum) },
            preLoadDataBlock = { pageNum -> if (showStickTop && pageNum == 0) this::loadStickyArticleList.invoke() else emptyList() }
        ).toPager()
    }
}