package com.maureen.wandevelop.feature.profile.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.maureen.wandevelop.base.WanAndroidPagePagingSource
import com.maureen.wandevelop.entity.MessageInfo
import com.maureen.wandevelop.ext.toPager
import com.maureen.wandevelop.feature.profile.ProfileRepository
import com.maureen.wandevelop.network.WanAndroidService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author lianml
 * @date 2024/5/15
 */
class NotificationViewModel : ViewModel() {
    companion object {
        private const val TAG = "NotificationViewModel"
    }

    private val repository = ProfileRepository()
    private val _showReadMsgListState = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            repository.updateUnreadMsgCount(0)
        }
    }

    private val unreadMsgPagingFlow: Flow<PagingData<MessageInfo>> by lazy {
        WanAndroidPagePagingSource(loadDataBlock = { pageNum ->
            WanAndroidService.instance.unreadMessageList(pageNum)
        }).toPager().flow
    }

    private val readMsgPagingFlow: Flow<PagingData<MessageInfo>> by lazy {
        WanAndroidPagePagingSource(loadDataBlock = { pageNum ->
            WanAndroidService.instance.readMessageList(pageNum)
        }).toPager().flow
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val messageListFlow: Flow<PagingData<MessageInfo>> = _showReadMsgListState.flatMapLatest {
        if (it){
            readMsgPagingFlow
        } else {
            unreadMsgPagingFlow
        }
    }.cachedIn(viewModelScope)

    fun showReadMsgList(unreadMsgCount: Int = 0) {
        if (unreadMsgCount > 0) {
            _showReadMsgListState.value = false
            return
        }
        _showReadMsgListState.update { it.not() }
    }

    fun refresh() {

    }
}