package com.maureen.wandevelop.network

import android.content.Context
import android.text.TextUtils.split
import android.util.Log
import com.maureen.wandevelop.MyApplication
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Function: 请求Cookie相关操作
 * Date:   2021/8/17
 * @author lianml
 */
class CustomCookieJar(private val context: Context) : CookieJar {
    companion object {
        private const val TAG = "CustomCookieJar"
        private const val SP_COOKIE_FILE_NAME = "cookie"
    }
    private val cookieCache = mutableMapOf<String, List<Cookie>>()

    init {
        // 初始化从sp中加载cookie到内存
        val cookieSp = context.getSharedPreferences(SP_COOKIE_FILE_NAME, Context.MODE_PRIVATE)
        cookieSp.all.entries
            .filter { it.value !is String }
            .map {
                Log.d(TAG, "${it.key}: ${it.value}")
                val cookies = (it.value as String).split(";")
                    .mapNotNull { cookieStr -> Cookie.parse(it.key.toHttpUrl(), cookieStr) }
                    .filter { cookie ->  !cookie.persistent || cookie.expiresAt < System.currentTimeMillis() }
                Pair(it.key, cookies)
            }.forEach {
                cookieCache[it.first] = it.second
            }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieCache[url.toString()] ?: emptyList()
    }
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val hasCookie = url.toString().startsWith("https://www.wanandroid.com/user/login")
        if (hasCookie) {
            cookieCache[url.toString()] = cookies
            val cookieStr = cookies.joinToString(";")
            Log.d(TAG, "saveFromResponse: $cookieStr")
            saveCookie(url, cookieStr)
        }
    }

    private fun saveCookie(url: HttpUrl, cookieStr: String) {
        context.getSharedPreferences(SP_COOKIE_FILE_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(url.toString(), cookieStr)
            .apply()
    }

    fun hasCookie(): Boolean {
        return cookieCache.isNotEmpty()
    }

    fun clearCookie() {
        context.getSharedPreferences(SP_COOKIE_FILE_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}