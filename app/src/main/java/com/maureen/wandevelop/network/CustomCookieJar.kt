package com.maureen.wandevelop.network

import android.util.Log
import com.maureen.wandevelop.util.UserPrefUtil
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

/**
 * Function: 请求Cookie相关操作
 * Date:   2021/8/17
 * @author lianml
 */
class CustomCookieJar : CookieJar {
    companion object {
        private const val TAG = "CustomCookieJar"
        private const val SAVE_USER_LOGIN_KEY = "${WanAndroidService.BASE_URL}user/login"
        private const val SAVE_USER_LOGOUT_KEY = "${WanAndroidService.BASE_URL}user/logout/json"
    }

    private val cookieCache = mutableListOf<Cookie>()

    init {
        // 初始化从dataStore中加载cookie到内存
        Log.d(TAG, "init: start")
        val cookieStr = UserPrefUtil.getPreferenceSync(TAG) ?: ""
        Log.d(TAG, "init: $cookieStr")
        cookieStr.split(";")
            .mapNotNull { str -> Cookie.parse(SAVE_USER_LOGIN_KEY.toHttpUrl(), str) }
            .filter { cookie -> cookie.persistent || cookie.expiresAt > System.currentTimeMillis() }
            .toCollection(cookieCache)
        Log.d(TAG, "init: ${cookieCache.size}")
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieCache.filter { cookie -> cookie.persistent || cookie.expiresAt > System.currentTimeMillis() }
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (url.toString().contains(SAVE_USER_LOGIN_KEY)) {
            // 登录时保存cookie缓存
            cookieCache.clear()
            cookieCache.addAll(cookies)
            UserPrefUtil.setPreferenceSync(TAG, cookies.joinToString(";"))
        } else if (url.toString().contains(SAVE_USER_LOGOUT_KEY)) {
            // 退出后清除cookie缓存
            cookieCache.clear()
            UserPrefUtil.setPreferenceSync(TAG, "")
        }
    }
}