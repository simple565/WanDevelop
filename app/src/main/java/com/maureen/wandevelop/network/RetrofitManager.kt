package com.maureen.wandevelop.network

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

    private const val DEFAULT_TIME_OUT: Long = 60

    val instance: Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(WanAndroidService.BASE_URL)
                .client(getOkHttpClint())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun getOkHttpClint(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

        return OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(logger)
                .build()
    }
}
