package com.maureen.wandevelop.data

import com.maureen.wandevelop.network.WanAndroidService

/**
 * Function: 账户相关
 * @author lianml
 * Create 2021-05-02
 */
object AccountRepository {

    suspend fun login(username: String, password: String) =
        WanAndroidService.instance.login(username, password).apiData()

}