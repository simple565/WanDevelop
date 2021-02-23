package com.maureen.wandevelop.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Function:
 * @author lianml
 * Create 2021-01-31
 */
interface WanAndroidService {

    companion object {
        private const val BASE_URL = "https://www.wanandroid.com/"

        fun create(): WanAndroidService {
            val logger = HttpLoggingInterceptor().apply { level = Level.BASIC }

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()

            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(WanAndroidService::class.java)
        }
    }

    @GET("article/top/json")
    suspend fun topArticleList(): BaseResponse<MutableList<ArticleBean>>

    @GET("article/list/{page}/json")
    suspend fun articleList(@Path("page") page: Int): BaseResponse<ArticleListBean>

}