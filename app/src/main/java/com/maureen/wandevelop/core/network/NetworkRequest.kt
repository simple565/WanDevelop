package com.maureen.wandevelop.core.network

import android.util.Log
import com.maureen.wandevelop.network.entity.BaseResponse

/**
 * @author lianml
 * @date 2025/10/5
 */
object NetworkRequest {
    private const val TAG = "BaseRepository"
    const val NETWORK_REQUEST_INTERVAL = 5 * 1000

    suspend fun <T> requestSafely(apiMethod: suspend () -> BaseResponse<T>): BaseResponse<T> {
        return try {
            apiMethod.invoke()
        } catch (e: Exception) {
            Log.e(TAG, "requestSafely: ", e)
            e.parse().run { BaseResponse(null, this.msg, this.code) }
        }
    }

    suspend fun <T> request(apiMethod: suspend () -> BaseResponse<T>): BaseResponse<T> {
        return apiMethod.invoke()
    }
}