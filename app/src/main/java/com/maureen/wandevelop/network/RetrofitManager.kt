package com.maureen.wandevelop.network

import android.util.Log
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
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
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val okHttpClient by lazy {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(logger)
            /*.cookieJar(object : CookieJar {
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    TODO("Not yet implemented")
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    cookies.forEach{
                        Log.i("TAG", "saveFromResponse: ${it.name} ${it.value}")
                    }
                }
            })*/
            .build()
    }
}
