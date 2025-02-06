package com.maureen.wandevelop.feature.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.maureen.wandevelop.entity.Banner
import com.maureen.wandevelop.ext.fixTags
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val repository = HomePageRepository()
    val articleFlow by lazy {
        repository.loadHomeArticleList().flow
            .map { it.map { data -> data.fixTags(application) } }
            .flowOn(Dispatchers.IO)
            .cachedIn(viewModelScope)
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.loadBannerData()
                .takeIf { it.isSuccess && it.data?.isNotEmpty() == true }
                ?.also {
                    _uiState.emit(HomeUiState.LoadBannerResult(it.data!!))
                }
        }
    }
}

sealed class HomeUiState {
    data object Idle : HomeUiState()
    data class LoadBannerResult(val bannerData: List<Banner>) : HomeUiState()
}