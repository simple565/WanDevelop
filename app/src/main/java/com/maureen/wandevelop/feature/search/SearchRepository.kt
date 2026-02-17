package com.maureen.wandevelop.feature.search

import androidx.paging.PagingData
import com.maureen.wandevelop.core.ext.newWanAndroidPager
import com.maureen.wandevelop.core.network.NetworkRequest
import com.maureen.wandevelop.db.AppDatabase
import com.maureen.wandevelop.db.SearchKey
import com.maureen.wandevelop.network.WanAndroidService
import com.maureen.wandevelop.network.entity.ArticleInfo
import com.maureen.wandevelop.network.entity.HotkeyInfo
import com.maureen.wandevelop.network.entity.PopularSectionItem
import com.maureen.wandevelop.util.UserPrefUtil
import com.maureen.wandevelop.util.UserPrefUtil.KEY_SHOW_HOTKEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * @author lianml
 * @date 2025/2/3
 */
class SearchRepository {
    companion object {
        const val KEY_SEARCH_KEYWORD = "KEY_SEARCH_KEYWORD"
        const val KEY_IS_DELETING_HISTORY = "KEY_IS_DELETING_HISTORY"
    }

    private val keyDao by lazy { AppDatabase.instance.searchKeyDao() }

    suspend fun saveSearchKey(keyword: String, timestamp: Long = System.currentTimeMillis()) = withContext(Dispatchers.IO) {
        keyDao.insertSearchKey(
            SearchKey(
                key = keyword,
                type = SearchKey.RECENT,
                timestamp = timestamp
            )
        )
    }

    suspend fun deleteSearchKey(keywords: List<String>) = withContext(Dispatchers.IO) {
        keyDao.deleteSearchRecentKey(keywords)
    }

    fun getSearchResultFlow(keyword: String): Flow<PagingData<ArticleInfo>> {
        return newWanAndroidPager(loadDataBlock = { pageNum ->
            WanAndroidService.instance.search(pageNum, keyword)
        }).flow
    }

    fun getHistoryKeyFlow(): Flow<List<SearchKey>> {
        return keyDao.querySearchRecentKey()
    }

    fun getHotkeyListVisibilityFlow(): Flow<Boolean> {
        return UserPrefUtil.getPreferenceFlow(key = KEY_SHOW_HOTKEY, defaultValue = true.toString())
            .map { it?.toBoolean() ?: true }
    }

    suspend fun setHotkeyListVisibility(visible: Boolean) {
        UserPrefUtil.setPreference(key = KEY_SHOW_HOTKEY, value = visible.toString())
    }

    fun getHotkeyList(): Flow<List<HotkeyInfo>> {
        return flow {
            val fromDb = keyDao.queryHotkey().first()
            if (fromDb.isNotEmpty()) {
                emit(fromDb.toHotkeyList())
            }
            val timestamp = System.currentTimeMillis()
            if (timestamp - (fromDb.firstOrNull()?.timestamp ?: 0) < NetworkRequest.NETWORK_REQUEST_INTERVAL) {
                return@flow
            }
            val response = NetworkRequest.requestSafely { WanAndroidService.instance.hotKey() }
            if (response.isSuccessWithData) {
                keyDao.clearHotkey()
                response.data!!.map { SearchKey(it.name, SearchKey.HOTKEY, timestamp) }
                    .also { keyDao.insertSearchKey(*it.toTypedArray()) }
                emit(response.data)
            }
        }
    }

    private fun List<SearchKey>.toHotkeyList(): List<HotkeyInfo> {
        return this.mapIndexed { index, searchKey ->
            HotkeyInfo(
                id = index,
                name = searchKey.key,
                order = index
            )
        }
    }

    suspend fun getPopularSectionList(): List<Pair<String, List<PopularSectionItem>>> = withContext(Dispatchers.IO) {
        val qa = async { NetworkRequest.requestSafely { WanAndroidService.instance.popularQa() } }
        val route = async { NetworkRequest.requestSafely { WanAndroidService.instance.popularRoute() } }
        val column = async { NetworkRequest.requestSafely { WanAndroidService.instance.popularColumn() } }
        return@withContext listOf(
            "最新学习路线" to (route.await().data?.map { PopularSectionItem(title = it.name, url = "https://www.wanandroid.com/route/show/${it.id}") } ?: emptyList()),
            "最受欢迎问答" to (qa.await().data?.map { PopularSectionItem(title = it.title, url = it.link) }  ?: emptyList()),
            "最受欢迎专栏" to (column.await().data?.map { PopularSectionItem(title = it.name, url = it.url) }  ?: emptyList())
        )
    }
}