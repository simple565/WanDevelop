package com.maureen.wandevelop.repository

import com.maureen.wandevelop.entity.BaseResponse
import com.maureen.wandevelop.entity.UserDetailInfo
import com.maureen.wandevelop.network.WanAndroidService

/**
 * Function: 账户相关
 * @author lianml
 * Create 2021-05-02
 */
class UserRepository {

    suspend fun getUserDetailInfo(): BaseResponse<UserDetailInfo> =
        WanAndroidService.instance.getUserDetailInfo()

    suspend fun signUp(username: String, password: String, passwordAgain: String) =
        WanAndroidService.instance.register(username, password, passwordAgain)

    suspend fun signIn(username: String, password: String) =
        WanAndroidService.instance.login(username, password)

    suspend fun logout() = WanAndroidService.instance.logout()

}