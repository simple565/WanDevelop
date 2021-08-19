package com.maureen.wandevelop.network

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * Function:
 * Date:   2021/8/17
 * @author lianml
 */
class CacheInterceptor(var interval: Int = 30) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
            .maxAge(interval, TimeUnit.MINUTES)
            .build()
        return response.newBuilder()
            .removeHeader("Pragma")
            .header("Cache-Control", cacheControl.toString())
            .build()
    }
}