package com.maureen.wandevelop.network

import retrofit2.http.*

/**
 * Function:
 * @author lianml
 * Create 2021-01-31
 */
interface WanAndroidService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com/"

        fun create(): WanAndroidService {
            return RetrofitManager.instance.create(WanAndroidService::class.java)
        }
    }

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): BaseResponse<UserInfo>

    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): BaseResponse<UserInfo>

    @GET("user/logout/json")
    suspend fun logout()

    @GET("banner/json")
    suspend fun banner(): BaseResponse<MutableList<Banner>>

    @GET("article/top/json")
    suspend fun stickyArticleList(): BaseResponse<MutableList<ArticleBean>>

    @GET("article/list/{page}/json")
    suspend fun articleList(@Path("page") page: Int): BaseResponse<ArticleListBean>

    @GET("hotkey/json")
    suspend fun hotKey(): BaseResponse<HotKey>

    @FormUrlEncoded
    @POST("article/query/{page}/json")
    suspend fun search(
        @Path("page") page: Int,
        @Field("k") key: String
    ): BaseResponse<ArticleListBean>
}