package com.maureen.wandevelop.data

import com.maureen.wandevelop.network.WanAndroidService

/**
 * Function: 账户相关
 * @author lianml
 * Create 2021-05-02
 */
object AccountRepository {

    suspend fun register(username: String, password: String, passwordAgain: String) =
        WanAndroidService.instance.register(username, password, passwordAgain)

    suspend fun login(username: String, password: String) =
        WanAndroidService.instance.login(username, password)

    suspend fun logout() = WanAndroidService.instance.logout()
}