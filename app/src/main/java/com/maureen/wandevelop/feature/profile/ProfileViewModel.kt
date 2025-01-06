package com.maureen.wandevelop.feature.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.maureen.wandevelop.entity.ProfileInfo
import com.maureen.wandevelop.entity.UserDetailInfo
import com.maureen.wandevelop.ext.cacheSize
import com.maureen.wandevelop.util.DarkModeUtil
import com.maureen.wandevelop.util.UserPrefUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Function: 我的页面 ViewModel
 * @author lianml
 * Create 2021-07-11
 */
class ProfileViewModel(private val application: Application) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "ProfileViewModel"
    }

    private val repository = UserRepository()
    private val _profileInfoFlow = MutableStateFlow(
        ProfileInfo(
            darkModeState = DarkModeUtil.getDarkModeName(application),
            cacheSize = application.cacheSize
        )
    )
    val profileInfoFlow = _profileInfoFlow.asStateFlow()
    val alreadyLogin: Boolean
        get() = _profileInfoFlow.value.userDetailInfo != null

    fun loadProfile() = viewModelScope.launch(Dispatchers.IO) {
        val currentTime = System.currentTimeMillis()
        val lastRequestTime = UserPrefUtil.getPreference(UserPrefUtil.KEY_LAST_REQUEST_USER_DETAIL)?.toLong() ?: 0L
        val userDetailInfo = UserPrefUtil.getPreference(UserPrefUtil.KEY_USER_DETAIL)
            .run { if (this.isNullOrBlank()) null else Json.decodeFromString<UserDetailInfo>(this) }
        val new = _profileInfoFlow.value.copy(
            userDetailInfo = userDetailInfo,
            darkModeState = DarkModeUtil.getDarkModeNameFromPreference(application),
            cacheSize = application.cacheSize
        )
        _profileInfoFlow.emit(new)
        if (currentTime - lastRequestTime <= 5 * 1000 || new.userDetailInfo == null) {
            // 5s时间内重复获取用户信息、用户未登录不进行请求
            return@launch
        }
        val response = repository.getUserDetailInfo()
        UserPrefUtil.setPreference(UserPrefUtil.KEY_LAST_REQUEST_USER_DETAIL, currentTime)
        if (response.isSuccessWithData) {
            val unreadCount = repository.getUnreadMessageCount().data ?: 0
            _profileInfoFlow.emit(new.copy(userDetailInfo = response.data, unreadCount = unreadCount))
            UserPrefUtil.setPreference(UserPrefUtil.KEY_USER_DETAIL, Json.encodeToString(response.data))
        } else {
            _profileInfoFlow.emit(new)
        }
    }

    suspend fun logoutAndClear() = withContext(Dispatchers.IO) {
        repository.logout()
        UserPrefUtil.setPreference(UserPrefUtil.KEY_LAST_REQUEST_USER_DETAIL, "0")
        UserPrefUtil.setPreference(UserPrefUtil.KEY_USER_DETAIL, "")
        val new = _profileInfoFlow.value.copy(
            userDetailInfo = null,
            darkModeState = DarkModeUtil.getDarkModeNameFromPreference(application),
            cacheSize = application.cacheSize
        )
        _profileInfoFlow.emit(new)
    }
}