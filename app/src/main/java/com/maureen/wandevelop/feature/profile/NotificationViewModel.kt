package com.maureen.wandevelop.feature.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.maureen.wandevelop.base.getCommonPager
import com.maureen.wandevelop.entity.MessageInfo
import com.maureen.wandevelop.network.WanAndroidService
import com.maureen.wandevelop.network.parse
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 * @author lianml
 * @date 2024/5/15
 */
class NotificationViewModel : ViewModel() {
    companion object {
        private const val TAG = "NotificationViewModel"
    }
    private var messageFlow: Flow<PagingData<MessageInfo>>? = null
    val deferred = CompletableDeferred<Boolean>()

    init {
        // 先请求未读消息列表，保证未读消息都转为已读消息
        viewModelScope.launch(Dispatchers.IO) {
            try {
                WanAndroidService.instance.unreadMessageList(1)
                deferred.complete(true)
            } catch (e: Exception) {
                Log.e(TAG, "request unread message : ${e.parse().msg}")
                deferred.complete(false)
            }
        }
    }

    fun loadReadMessageList(): Flow<PagingData<MessageInfo>> {
        return messageFlow ?: getCommonPager { pageNum -> WanAndroidService.instance.readMessageList(pageNum) }
                .flow.flowOn(Dispatchers.IO).cachedIn(viewModelScope).also { messageFlow = it }
    }

}