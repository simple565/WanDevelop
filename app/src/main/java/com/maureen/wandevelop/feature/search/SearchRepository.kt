package com.maureen.wandevelop.feature.search

import androidx.paging.PagingData
import com.maureen.wandevelop.base.BaseRepository
import com.maureen.wandevelop.db.AppDatabase
import com.maureen.wandevelop.db.SearchKey
import com.maureen.wandevelop.entity.ArticleInfo
import com.maureen.wandevelop.entity.HotkeyInfo
import com.maureen.wandevelop.ext.newWanAndroidPager
import com.maureen.wandevelop.network.WanAndroidService
import com.maureen.wandevelop.util.UserPrefUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * @author lianml
 * @date 2025/2/3
 */
class SearchRepository : BaseRepository() {
    companion object {
        const val KEY_SEARCH_KEYWORD = "KEY_SEARCH_KEYWORD"
        const val KEY_IS_DELETING_HISTORY = "KEY_IS_DELETING_HISTORY"
        private const val KEY_SHOW_HOTKEY = "KEY_SHOW_HOTKEY"
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
            if (timestamp - (fromDb.firstOrNull()?.timestamp ?: 0) < NETWORK_REQUEST_INTERVAL) {
                return@flow
            }
            val response = requestSafely { WanAndroidService.instance.hotKey() }
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


}