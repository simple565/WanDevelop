package com.maureen.wandevelop.core.network

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * 缓存拦截器
 * Date:   2021/8/17
 */
class CacheInterceptor(private val interval: Int = 30) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
            .maxAge(interval, TimeUnit.SECONDS)
            .build()
        return response.newBuilder()
            .removeHeader("Pragma")
            .header("Cache-Control", cacheControl.toString())
            .build()
    }
}