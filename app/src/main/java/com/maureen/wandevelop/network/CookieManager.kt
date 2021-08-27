package com.maureen.wandevelop.network

import android.webkit.CookieManager
import com.google.gson.Gson
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * Function:
 * Date:   2021/8/17
 * @author lianml
 */
class CookieManager : CookieJar {
    companion object {
        private const val TAG = "CookieManager"
    }

    private var cookieList = mutableListOf<Cookie>()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieList
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val isSaveCookies = url.toString().startsWith("https://www.wanandroid.com/user/login")
        if (isSaveCookies) {
            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            cookies.forEach {
                cookieManager.setCookie(it.domain, "${it.name}=${it.value}")
            }
            cookieManager.flush()
            cookieList.addAll(cookies)
            val cookieGson = Gson().toJson(cookies)
        }
    }
}