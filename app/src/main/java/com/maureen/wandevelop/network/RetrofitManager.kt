package com.maureen.wandevelop.network

import com.maureen.wandevelop.MyApplication
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Function:
 * @author lianml
 * Create 2021-05-01
 */
object RetrofitManager {

    private const val DEFAULT_TIME_OUT: Long = 60L
    private const val MAX_CACHE_SIZE: Long = 10 * 1024 * 1024

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(WanAndroidService.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val okHttpClient by lazy {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.HEADERS }
        OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(logger)
            .addNetworkInterceptor(CacheInterceptor())
            .cache(Cache(MyApplication.instance.cacheDir, MAX_CACHE_SIZE))
            .cookieJar(CookieManager())
            .build()
    }
}
