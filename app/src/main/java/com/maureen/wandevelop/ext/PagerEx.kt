package com.maureen.wandevelop.ext

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.maureen.wandevelop.core.WanAndroidPagePagingSource
import com.maureen.wandevelop.entity.BasePage
import com.maureen.wandevelop.entity.BaseResponse
import com.maureen.wandevelop.network.WanAndroidService

/**
 * @author lianml
 * @date 2024/12/24
 */
fun <T : Any> newPager(
    pageSize: Int = WanAndroidService.DEFAULT_PAGE_SIZE,
    pagingSourceFactory: () -> PagingSource<Int, T>
): Pager<Int, T> {
    return Pager(
        PagingConfig(pageSize, enablePlaceholders = false),
        pagingSourceFactory = pagingSourceFactory
    )
}

fun <T : Any> newWanAndroidPager(
    pageSize: Int = WanAndroidService.DEFAULT_PAGE_SIZE,
    startPage: Int = WanAndroidService.DEFAULT_START_PAGE_INDEX,
    preLoadDataBlock: (suspend (Int) -> List<T>)? = null,
    loadDataBlock: suspend (Int) -> BaseResponse<BasePage<T>>
): Pager<Int, T> {
    return Pager(
        config = PagingConfig(pageSize, enablePlaceholders = false),
        pagingSourceFactory = {
            WanAndroidPagePagingSource(
                startPage = startPage,
                preLoadDataBlock = preLoadDataBlock,
                loadDataBlock = loadDataBlock
            )
        }
    )
}