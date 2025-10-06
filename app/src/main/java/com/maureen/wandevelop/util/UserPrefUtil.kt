package com.maureen.wandevelop.util

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.maureen.wandevelop.MyApplication
import com.maureen.wandevelop.core.ext.preferenceStore
import kotlinx.coroutines.flow.Flow
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
    const val KEY_SHOW_BANNER = "KEY_SHOW_BANNER"
    const val KEY_SHOW_STICK_TOP_ARTICLE = "KEY_SHOW_STICK_TOP_ARTICLE"
    const val KEY_SHOW_HOTKEY = "KEY_SHOW_HOTKEY"
    const val KEY_PRIVATE_MODE = "KEY_PRIVATE_MODE"
    const val KEY_USER_INFO = "KEY_USER_INFO"
    const val KEY_USER_UNREAD_MSG_COUNT = "KEY_USER_UNREAD_MSG_COUNT"
    const val KEY_USER_COLLECT_ID = "KEY_USER_COLLECT_ID"

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

    fun getPreferenceFlow(key: String, defaultValue: String? = null): Flow<String?> {
        return dataStore.data.map { it[stringPreferencesKey(key)] ?: defaultValue }
    }

    suspend fun setPreference(key: String, value: String) {
        dataStore.edit {
            it[stringPreferencesKey(key)] = value.toString()
        }
    }

    suspend fun setPreference(valueMap: Map<String, String>) {
        dataStore.edit {
            valueMap.forEach { entry ->
                it[stringPreferencesKey(entry.key)] = entry.value
            }
        }
    }
}