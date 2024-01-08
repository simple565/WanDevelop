package com.maureen.wandevelop.network

import com.maureen.wandevelop.entity.ArticleInfo
import com.maureen.wandevelop.entity.Banner
import com.maureen.wandevelop.entity.BasePage
import com.maureen.wandevelop.entity.BaseResponse
import com.maureen.wandevelop.entity.HotKey
import com.maureen.wandevelop.entity.UserDetailInfo
import com.maureen.wandevelop.entity.UserInfo
import retrofit2.http.*

/**
 * Function:
 * @author lianml
 * Create 2021-01-31
 */
interface WanAndroidService {

    companion object {
        private const val BASE_URL = "https://www.wanandroid.com/"

        val instance: WanAndroidService by lazy {
            RetrofitManager.createService(WanAndroidService::class.java, BASE_URL)
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
        @Field("repassword") rePassword: String
    ): BaseResponse<UserInfo>

    @GET("user/lg/userinfo/json")
    suspend fun getUserDetailInfo(): BaseResponse<UserDetailInfo>

    @GET("user/logout/json")
    suspend fun logout()

    @GET("banner/json")
    suspend fun banner(): BaseResponse<List<Banner>>

    @GET("article/top/json")
    suspend fun stickyArticleList(): BaseResponse<List<ArticleInfo>>

    @GET("article/list/{page}/json")
    suspend fun articleList(@Path("page") page: Int): BaseResponse<BasePage<ArticleInfo>>

    @GET("hotkey/json")
    suspend fun hotKey(): BaseResponse<HotKey>

    @FormUrlEncoded
    @POST("article/query/{page}/json")
    suspend fun search(
        @Path("page") page: Int,
        @Field("k") key: String
    ): BaseResponse<BasePage<ArticleInfo>>
}