package com.maureen.wandevelop.feature.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.maureen.wanandroid.UserPreferences
import com.maureen.wandevelop.entity.UserDetailInfo
import com.maureen.wandevelop.util.UserPrefUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 * Function: 我的页面 ViewModel
 * @author lianml
 * Create 2021-07-11
 */
class ProfileViewModel(private val application: Application) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "ProfileViewModel"
    }

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val uiState = _uiState.asStateFlow()
    var alreadyLogin = false
    private val repository = UserRepository()

    fun loadPreference() = viewModelScope.launch(Dispatchers.IO) {
        val userPreferences = UserPrefUtil.getPreference(application) ?: return@launch _uiState.emit(
            ProfileUiState.LoadUserPreference(null, 0)
        )
        val unreadCount = repository.getUnreadMessageCount().data ?: 0
        _uiState.emit(ProfileUiState.LoadUserPreference(userPreferences, unreadCount))
    }

    fun loadUserDetail() = viewModelScope.launch(Dispatchers.IO) {
        val response = repository.getUserDetailInfo()
        val result = if (response.isSuccess && response.data != null) {
            val unreadCount = repository.getUnreadMessageCount().data ?: 0
            ProfileUiState.LoadUserInfoSuccess(response.data, unreadCount)
        } else {
            ProfileUiState.LoadUserInfoFail("加载用户信息失败")
        }
        _uiState.emit(result)
    }

    fun logoutAndClear() = flow {
        val result = repository.logout().isSuccess
        UserPrefUtil.setPreference(application) { builder ->
            builder.clearCookie()
        }
        emit(result)
    }.flowOn(Dispatchers.IO)
}

sealed class ProfileUiState {
    data object Idle : ProfileUiState()
    data class LoadUserPreference(val preferences: UserPreferences?, val unreadCount: Int): ProfileUiState()
    data class LoadUserInfoSuccess(val userInfo: UserDetailInfo, val unreadCount: Int) : ProfileUiState()
    data class LoadUserInfoFail(val msg: String) : ProfileUiState()
}