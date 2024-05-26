package com.maureen.wandevelop.feature.profile

import com.maureen.wandevelop.base.BaseRepository
import com.maureen.wandevelop.entity.BaseResponse
import com.maureen.wandevelop.entity.UserDetailInfo
import com.maureen.wandevelop.network.WanAndroidService

/**
 * Function: 账户相关
 * @author lianml
 * Create 2021-05-02
 */
class UserRepository : BaseRepository() {

    suspend fun getUserDetailInfo() = request { WanAndroidService.instance.userDetailInfo() }

    suspend fun getUnreadMessageCount() = request { WanAndroidService.instance.unreadMessageCount() }

    suspend fun signUp(username: String, password: String, passwordAgain: String) =
        request { WanAndroidService.instance.register(username, password, passwordAgain) }

    suspend fun signIn(username: String, password: String) =
        request { WanAndroidService.instance.login(username, password) }

    suspend fun logout() = request { WanAndroidService.instance.logout() }
}