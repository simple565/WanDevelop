package com.maureen.wandevelop.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maureen.wandevelop.base.BaseRepository
import com.maureen.wandevelop.entity.ProfileInfo
import com.maureen.wandevelop.entity.toProfileInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * 我的页面 ViewModel
 */
class ProfileViewModel : ViewModel() {
    companion object {
        private const val TAG = "ProfileViewModel"
        private const val DEFAULT_USER_DATA = "--"
    }

    private val repository = ProfileRepository()
    private val requestTime: MutableStateFlow<Long> = MutableStateFlow(0L)

    val profileInfoState = repository.getProfileInfoFromCache().map {
        it.copy(
            level = if (it.level.isNotBlank()) String.format("Lv.%s", it.level) else DEFAULT_USER_DATA,
            coin = it.coin.ifBlank { DEFAULT_USER_DATA },
            rank = if (it.rank.isNotBlank()) String.format("No.%s", it.rank) else DEFAULT_USER_DATA
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ProfileInfo(rank = DEFAULT_USER_DATA, level = DEFAULT_USER_DATA, coin = DEFAULT_USER_DATA)
    )

    fun loadProfile() = viewModelScope.launch(Dispatchers.IO) {
        val requestAlready = System.currentTimeMillis() - requestTime.value <= BaseRepository.NETWORK_REQUEST_INTERVAL
        if (repository.hasProfileCache().not() || requestAlready) {
            // 用户未登录不进行请求、5s时间内重复请求用户信息
            return@launch
        }
        val response = repository.getUserDetailInfo()
        requestTime.value = System.currentTimeMillis()
        if (!response.isSuccessWithData) {
            return@launch
        }
        val unreadCount = repository.getUnreadMessageCount().data ?: 0
        response.data?.also {
            repository.saveProfileInfo(it.toProfileInfo().copy(unreadMsgCount = unreadCount))
            repository.saveUserCollectIds(it.userInfo.collectIds)
        }
    }
}