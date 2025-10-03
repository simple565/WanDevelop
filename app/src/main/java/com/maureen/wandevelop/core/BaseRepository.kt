package com.maureen.wandevelop.core

import android.util.Log
import com.maureen.wandevelop.entity.BaseResponse
import com.maureen.wandevelop.network.parse

/**
 * 基础仓库
 * @author lianml
 * @date 2024/1/2
 */
open class BaseRepository {
    companion object {
        private const val TAG = "BaseRepository"
        const val NETWORK_REQUEST_INTERVAL = 5 * 1000
    }

    open suspend fun <T> requestSafely(apiMethod: suspend () -> BaseResponse<T>): BaseResponse<T> {
        return try {
            apiMethod.invoke()
        } catch (e: Exception) {
            Log.e(TAG, "requestSafely: ", e)
            e.parse().run { BaseResponse(null, this.msg, this.code) }
        }
    }
    
    open suspend fun <T>request(apiMethod: suspend () -> BaseResponse<T>): BaseResponse<T> {
        return apiMethod.invoke()
    }
}