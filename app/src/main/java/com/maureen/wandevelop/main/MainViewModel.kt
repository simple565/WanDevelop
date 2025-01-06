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
}

sealed class MainUiState {
}