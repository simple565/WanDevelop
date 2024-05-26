package com.maureen.wandevelop.util

import android.content.Context
import android.util.Log
import com.maureen.wanandroid.UserPreferences
import com.maureen.wandevelop.ext.preferenceStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

/**
 * 用户偏好设置工具类
 * @author lianml
 * @date 2024/2/10
 */
object UserPrefUtil {
    private const val TAG = "UserPrefUtil"

    fun getPreferenceSync(context: Context): UserPreferences? {
        return runBlocking {
            context.preferenceStore.data.firstOrNull()
        }
    }

    fun setPreferenceSync(context: Context, script: (UserPreferences.Builder) -> Unit) {
        runBlocking {
            val begin = System.currentTimeMillis()
            setPreference(context, script)
            Log.d(TAG, "setPreferenceSync: spent ${System.currentTimeMillis() -  begin}")
        }
    }

    suspend fun getPreference(context: Context): UserPreferences? {
        return context.preferenceStore.data.firstOrNull()
    }

    suspend fun setPreference(context: Context, script: (UserPreferences.Builder) -> Unit) {
        context.preferenceStore.updateData {
            it.toBuilder()
                .apply { script.invoke(this) }
                .build()
        }
    }
}