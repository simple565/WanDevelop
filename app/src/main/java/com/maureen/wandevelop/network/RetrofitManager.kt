package com.maureen.wandevelop.network

import com.maureen.wandevelop.MyApplication
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Function:
 * @author lianml
 * Create 2021-05-01
 */
object RetrofitManager {
    private const val DEFAULT_TIME_OUT: Long = 60L
    private const val MAX_CACHE_SIZE: Long = 10 * 1024 * 1024
    private val cookieJar by lazy { CustomCookieJar(MyApplication.instance.applicationContext) }

    private val okHttpClient by lazy {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(logger)
            .addNetworkInterceptor(CacheInterceptor())
            .cache(Cache(MyApplication.instance.cacheDir, MAX_CACHE_SIZE))
            .cookieJar(cookieJar)
            .build()
    }

    fun <S> createService(serviceClass: Class<S>, baseUrl: String): S {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(baseUrl)
            .build().create(serviceClass)
    }

    fun hasCookie(): Boolean {
        return cookieJar.hasCookie()
    }

    fun clearCookie() {
        cookieJar.clearCookie()
    }
}
