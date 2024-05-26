package com.maureen.wandevelop.base

import android.util.Log
import com.maureen.wandevelop.entity.BaseResponse
import com.maureen.wandevelop.network.NetworkError
import com.maureen.wandevelop.network.parse
import com.squareup.moshi.JsonEncodingException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * 基础仓库
 * @author lianml
 * @date 2024/1/2
 */
open class BaseRepository {
    companion object {
        private const val TAG = "BaseRepository"
    }

    open suspend fun <T> request(apiMethod: suspend () -> BaseResponse<T>): BaseResponse<T> {
        return try {
            apiMethod.invoke()
        } catch (e: Exception) {
            e.parse().run { BaseResponse(null, this.msg, this.code) }
        }
    }
}