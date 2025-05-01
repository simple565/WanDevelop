package com.maureen.wandevelop.core

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.maureen.wandevelop.entity.BasePage
import com.maureen.wandevelop.entity.BaseResponse
import com.maureen.wandevelop.network.WanAndroidService
import com.maureen.wandevelop.network.parse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * WanAndroid 相关API
 * @author lianml
 * @date 2024/5/18
 */
class WanAndroidPagePagingSource<T : Any>(
    private val startPage: Int = WanAndroidService.DEFAULT_START_PAGE_INDEX,
    private val preLoadDataBlock: (suspend (Int) -> List<T>)? = null,
    private val loadDataBlock: suspend (Int) -> BaseResponse<BasePage<T>>
) : PagingSource<Int, T>() {
    companion object {
        private const val TAG = "WanAndroidPagePagingSource"
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return withContext(Dispatchers.IO) {
            try {
                val pageNum = params.key ?: startPage
                val result = loadDataBlock(pageNum)
                val totalPageCount = result.data?.pageCount ?: 0
                val preKey = if (pageNum > startPage) pageNum.minus(1) else null
                val maxPageNum = if (startPage == 0) totalPageCount.minus(1) else totalPageCount
                if (pageNum == maxPageNum) {
                    return@withContext LoadResult.Page(data = emptyList(), prevKey = preKey, nextKey = null)
                }
                if (!result.isSuccess || result.data == null) {
                    return@withContext  LoadResult.Error(Throwable(message = result.errorMsg))
                }
                val data = preLoadDataBlock?.invoke(pageNum)?.let { it + result.data.dataList } ?: result.data.dataList
                LoadResult.Page(data, prevKey = preKey, nextKey = pageNum.plus(1))
            } catch (e: Exception) {
                Log.e(TAG, "load: ", e)
                Log.d(TAG, "load: ${e.parse().msg}")
                LoadResult.Error(e)
            }
        }
    }
}