package com.maureen.wandevelop.network

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * Function:
 * Date:   2021/8/17
 * @author lianml
 */
class CookieManager: CookieJar {
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        TODO("Not yet implemented")
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        TODO("Not yet implemented")
    }
}