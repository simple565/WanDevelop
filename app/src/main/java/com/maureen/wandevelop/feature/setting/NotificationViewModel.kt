package com.maureen.wandevelop.feature.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.maureen.wandevelop.base.WanAndroidPagePagingSource
import com.maureen.wandevelop.entity.MessageInfo
import com.maureen.wandevelop.ext.toPager
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
    val deferred = CompletableDeferred<Boolean>()
    val messageFlow: Flow<PagingData<MessageInfo>> by lazy {
        WanAndroidPagePagingSource(loadDataBlock = { pageNum ->
            WanAndroidService.instance.readMessageList(pageNum)
        }).toPager().flow.flowOn(Dispatchers.IO).cachedIn(viewModelScope)
    }

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
}