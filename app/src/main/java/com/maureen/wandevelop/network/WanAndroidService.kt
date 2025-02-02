package com.maureen.wandevelop.network

import com.maureen.wandevelop.entity.Article
import com.maureen.wandevelop.entity.Banner
import com.maureen.wandevelop.entity.BasePage
import com.maureen.wandevelop.entity.BaseResponse
import com.maureen.wandevelop.entity.Collection
import com.maureen.wandevelop.entity.HotKey
import com.maureen.wandevelop.entity.MessageInfo
import com.maureen.wandevelop.entity.SharerInfo
import com.maureen.wandevelop.entity.TreeInfo
import com.maureen.wandevelop.entity.UserDetailInfo
import com.maureen.wandevelop.entity.UserInfo
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Function: WanAndroid 网站API
 * @author lianml
 * Create 2021-01-31
 */
interface WanAndroidService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com/"
        const val DEFAULT_PAGE_SIZE = 20
        const val DEFAULT_START_PAGE_INDEX = 0
        const val DEFAULT_START_PAGE_INDEX_ALIAS = 1

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
    suspend fun userDetailInfo(): BaseResponse<UserDetailInfo>

    @GET("user/logout/json")
    suspend fun logout(): BaseResponse<String>

    /**
     * 未读消息数量
     */
    @GET("message/lg/count_unread/json")
    suspend fun unreadMessageCount(): BaseResponse<Int>

    /**
     * 未读消息列表
     * @param page 从1开始
     */
    @GET("message/lg/unread_list/{page}/json")
    suspend fun unreadMessageList(@Path("page") page: Int): BaseResponse<BasePage<MessageInfo>>

    /**
     * 已读消息列表
     * @param page 从1开始
     */
    @GET("message/lg/readed_list/{page}/json")
    suspend fun readMessageList(@Path("page") page: Int): BaseResponse<BasePage<MessageInfo>>

    @GET("banner/json")
    suspend fun banner(): BaseResponse<List<Banner>>

    /**
     * 置顶文章列表
     */
    @GET("article/top/json")
    suspend fun stickyArticleList(): BaseResponse<List<Article>>

    /**
     * 首页文章列表
     * @param page 从0开始
     */
    @GET("article/list/{page}/json")
    suspend fun homeArticleList(@Path("page") page: Int): BaseResponse<BasePage<Article>>

    /**
     * 体系数据列表
     */
    @GET("tree/json")
    suspend fun treeList(): BaseResponse<List<TreeInfo>>

    /**
     * 收藏文章列表
     * @param page 从0开始
     */
    @GET("lg/collect/list/{page}/json")
    suspend fun collectionList(@Path("page") page: Int): BaseResponse<BasePage<Collection>>

    /**
     * 收藏文章
     */
    @FormUrlEncoded
    @POST("lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Long): BaseResponse<String?>

    /**
     * 已收藏列表取消收藏
     */
    @FormUrlEncoded
    @POST("lg/uncollect/{id}/json")
    suspend fun cancelCollect(@Path("id") id: Long, @Field("originId") key: Long): BaseResponse<String?>

    /**
     * 文章列表取消收藏
     */
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun cancelCollect(@Path("id") id: Long): BaseResponse<String?>

    /**
     * 自己分享的文章
     * @param page 从1开始
     */
    @GET("user/lg/private_articles/{page}/json")
    suspend fun myShareList(@Path("page") page: Int): BaseResponse<BasePage<Article>>

    /**
     * 热门搜索关键词
     */
    @GET("hotkey/json")
    suspend fun hotKey(): BaseResponse<HotKey>

    /**
     * 搜索文章
     * @param page 从0开始
     */
    @FormUrlEncoded
    @POST("article/query/{page}/json")
    suspend fun search(
        @Path("page") page: Int,
        @Field("k") key: String
    ): BaseResponse<BasePage<Article>>

    /**
     * 根据作者昵称搜索文章列表
     * @param page 从0开始
     */
    @GET("article/list/{page}/json")
    suspend fun searchByAuthorNickname(
        @Path("page") page: Int,
        @Query("author") authorId: Int
    ): BaseResponse<BasePage<Article>>

    /**
     * 分享人信息及分享文章列表
     * @param page 从1开始
     */
    @GET("user/{id}/share_articles/{page}/json")
    suspend fun sharerInfo(
        @Path("id") userId: Int,
        @Path("page") page: Int
    ): BaseResponse<SharerInfo>
}