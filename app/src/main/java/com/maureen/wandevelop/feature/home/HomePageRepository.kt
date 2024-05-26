package com.maureen.wandevelop.feature.home

import com.maureen.wandevelop.network.WanAndroidService

/**
 * Function: 首页相关
 * @author lianml
 * Create 2021-02-17
 */
object HomePageRepository {

    /**
     * 获取置顶文章
     */
    suspend fun loadStickyArticleList() = WanAndroidService.instance.stickyArticleList()

    /**
     * 首页文章列表
     */
    suspend fun loadHomeArticleList(pageIndex: Int) = WanAndroidService.instance.articleList(pageIndex)
}