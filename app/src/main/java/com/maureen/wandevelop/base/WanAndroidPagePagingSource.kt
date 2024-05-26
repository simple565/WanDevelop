package com.maureen.wandevelop.base

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.maureen.wandevelop.base.BasePagePagingSource.Companion.DEFAULT_PAGE_SIZE
import com.maureen.wandevelop.entity.BasePage
import com.maureen.wandevelop.entity.BaseResponse
import com.maureen.wandevelop.network.NetworkError
import com.maureen.wandevelop.network.NoMoreException
import retrofit2.HttpException
import retrofit2.Response

/**
 * WanAndroid 相关API
 * @author lianml
 * @date 2024/5/18
 */
class BasePagePagingSource<T : Any>(private val apiBlock: suspend (Int) -> BaseResponse<BasePage<T>>) : PagingSource<Int, T>() {
    companion object {
        private const val TAG = "BasePagePagingSource"
        const val DEFAULT_PAGE_SIZE = 10
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val pageNum = params.key ?: 1
            val result = apiBlock(pageNum)
            val totalPageCount = result.data?.pageCount ?: 0
            if (pageNum > totalPageCount) {
                return LoadResult.Error(NoMoreException())
            }
            val preKey = if (pageNum > 1) pageNum.minus(1) else null
            LoadResult.Page(result.data?.dataList!!, prevKey = preKey, nextKey = pageNum.plus(1))
        } catch (e: Exception) {
            Log.d(TAG, "load: ${e.message}")
            LoadResult.Error(e)
        }
    }
}

fun <T: Any>getCommonPager(pageSize: Int = DEFAULT_PAGE_SIZE, apiBlock: suspend (Int) -> BaseResponse<BasePage<T>>): Pager<Int, T> {
    return Pager(
        config = PagingConfig(pageSize, enablePlaceholders = false),
        pagingSourceFactory = { BasePagePagingSource(apiBlock) })
}