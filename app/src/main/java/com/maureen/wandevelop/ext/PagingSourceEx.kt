package com.maureen.wandevelop.ext

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.maureen.wandevelop.network.WanAndroidService

/**
 * @author lianml
 * @date 2024/12/24
 */
fun <T : Any> PagingSource<Int, T>.toPager(pageSize: Int = WanAndroidService.DEFAULT_PAGE_SIZE): Pager<Int, T> {
    return Pager(PagingConfig(pageSize, enablePlaceholders = false), pagingSourceFactory = { this })
}