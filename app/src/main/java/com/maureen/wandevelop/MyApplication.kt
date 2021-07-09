package com.maureen.wandevelop

import android.app.Application

/**
 * Function:
 * Date:   2021/7/9
 * @author lianml
 */
class MyApplication : Application() {

    companion object {
        lateinit var instance: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}