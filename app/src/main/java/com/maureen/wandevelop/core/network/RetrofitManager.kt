package com.maureen.wandevelop.core.network

import com.maureen.wandevelop.MyApplication
import com.maureen.wandevelop.network.CustomCookieJar
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Function:
 * @author lianml
 * Create 2021-05-01
 */
object RetrofitManager {
    private const val DEFAULT_TIME_OUT: Long = 60L
    private const val MAX_CACHE_SIZE: Long = 10 * 1024 * 1024

    private val okHttpClient by lazy {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .hostnameVerifier { hostname, session -> return@hostnameVerifier true }
            .addInterceptor(logger)
            .addNetworkInterceptor(CacheInterceptor())
            .cache(Cache(MyApplication.instance.cacheDir, MAX_CACHE_SIZE))
            .cookieJar(CustomCookieJar())
            .build()
    }

    fun <S> createService(serviceClass: Class<S>, baseUrl: String): S {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .build().create(serviceClass)
    }
}
