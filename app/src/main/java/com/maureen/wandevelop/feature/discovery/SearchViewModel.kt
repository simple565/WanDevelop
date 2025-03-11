package com.maureen.wandevelop.feature.discovery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.maureen.wandevelop.MyApplication
import com.maureen.wandevelop.common.entity.Feed
import com.maureen.wandevelop.entity.HotkeyInfo
import com.maureen.wandevelop.ext.toFeed
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * @author lianml
 * @date 2025/2/3
 */
class SearchViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val repository = SearchRepository()

    val searchKeywordState: StateFlow<Pair<String, Boolean>> = savedStateHandle.getStateFlow(
        key = SearchRepository.KEY_SEARCH_KEYWORD,
        initialValue = Pair("", false)
    )
    private val keyDeleteState: StateFlow<Boolean> = savedStateHandle.getStateFlow(
        key = SearchRepository.KEY_IS_DELETING_HISTORY,
        initialValue = false
    )

    val searchResultFlow: StateFlow<PagingData<Feed>> = searchKeywordState.flatMapLatest {
        if (it.first.isBlank() || it.second.not()) {
            flowOf(PagingData.empty())
        } else {
            repository.getSearchResultFlow(it.first)
                .map { paging -> paging.map { article -> article.toFeed(MyApplication.instance.applicationContext) } }
        }
    }.cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PagingData.empty()
        )

    val hotkeyInfoUiState: StateFlow<Pair<Boolean, List<HotkeyInfo>>> =
        repository.getHotkeyListVisibilityFlow().flatMapLatest {
            if (it) {
                repository.getHotkeyList().mapLatest { hotkeys -> Pair(true, hotkeys) }
            } else {
                flowOf(Pair(false, emptyList()))
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Pair(false, emptyList())
        )

    val historyKeyUiState: StateFlow<Pair<Boolean, List<String>>> =
        combine(repository.getHistoryKeyFlow(), keyDeleteState) { hotkeyList, deleteState ->
            Pair(deleteState, hotkeyList.map { key -> key.key })
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Pair(false, emptyList())
        )

    fun onSearchKeywordChanged(keyword: String, confirm: Boolean = false) = viewModelScope.launch {
        savedStateHandle[SearchRepository.KEY_SEARCH_KEYWORD] = keyword to confirm
        if (confirm.not()) {
            return@launch
        }
        repository.saveSearchKey(keyword)
    }

    fun deleteSearchHistory(historyList: List<String>?) = viewModelScope.launch {
        savedStateHandle[SearchRepository.KEY_IS_DELETING_HISTORY] = null != historyList
        if (historyList.isNullOrEmpty()) {
            return@launch
        }
        repository.deleteSearchKey(historyList)
    }

    /**
     * 更改热搜词列表是否可见
     */
    fun updateHotkeyVisibility(visible: Boolean) = viewModelScope.launch {
        repository.setHotkeyListVisibility(visible)
    }
}