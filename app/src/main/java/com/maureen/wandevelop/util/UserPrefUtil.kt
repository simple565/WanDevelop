package com.maureen.wandevelop.util

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.maureen.wandevelop.MyApplication
import com.maureen.wandevelop.ext.preferenceStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 * 用户偏好设置工具类
 * @author lianml
 * @date 2024/2/10
 */
object UserPrefUtil {
    private const val TAG = "UserPrefUtil"
    const val KEY_USER_DETAIL = "KEY_USER_DETAIL"
    const val KEY_LAST_REQUEST_USER_DETAIL = "KEY_LAST_REQUEST_USER_DETAIL"

    private val dataStore by lazy {
        MyApplication.instance.preferenceStore
    }

    fun getPreferenceSync(key: String): String? {
        return runBlocking {
            getPreference(key)
        }
    }

    fun setPreferenceSync(key: String, value: String) {
        runBlocking {
            setPreference(key, value)
        }
    }

    suspend fun getPreference(key: String): String? {
        return dataStore.data.map { it[stringPreferencesKey(key)] }.firstOrNull()
    }

    suspend fun <T> setPreference(key: String, value: T) {
        dataStore.edit {
            it[stringPreferencesKey(key)] = value.toString()
        }
    }
}