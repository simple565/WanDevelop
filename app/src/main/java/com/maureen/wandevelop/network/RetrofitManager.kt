package com.maureen.wandevelop.network

import android.util.Log
import com.maureen.wandevelop.MyApplication
import okhttp3.*
import okhttp3.CacheControl
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
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(logger)
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                        .newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
                    return chain.proceed(request)
                }
            })
            .addNetworkInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val response = chain.proceed(chain.request())
                    // 缓存 10 秒
                    val cacheControl = CacheControl.Builder()
                        .maxAge(120, TimeUnit.SECONDS)
                        .build()
                    return response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", cacheControl.toString())
                        .build()
                }
            })
            .cache(Cache(MyApplication.instance.cacheDir, MAX_CACHE_SIZE))
            .cookieJar(object : CookieJar {
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    return emptyList()
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    cookies.forEach {
                        Log.i("TAG", "saveFromResponse: ${it.name} ${it.value}")
                    }
                }
            })
            .build()
    }
}
