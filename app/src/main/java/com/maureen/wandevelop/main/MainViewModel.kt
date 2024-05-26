package com.maureen.wandevelop.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.maureen.wandevelop.network.WanAndroidService
import com.maureen.wandevelop.util.DarkModeUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * @author lianml
 * @date 2024/2/16
 */
class MainViewModel(private val application: Application): AndroidViewModel(application) {
    companion object {
        private const val TAG = "MainViewModel"
    }
    private val _uiState = MutableSharedFlow<MainUiState>()
    val uiState = _uiState.asSharedFlow()

    init {
        viewModelScope.launch {
            DarkModeUtil.initFromPreference(application)
        }
    }

    fun loadUnreadMsgCount() = viewModelScope.launch(Dispatchers.IO) {
        val response = WanAndroidService.instance.unreadMessageCount()
        if (response.isSuccess) {
            Log.d(TAG, "loadUnreadMsgCount: ${response.data}")
            _uiState.emit(MainUiState.NotificationCount(response.data ?: 0))
        }
    }
}

sealed class MainUiState {
    data class NotificationCount(val count: Int): MainUiState()
}