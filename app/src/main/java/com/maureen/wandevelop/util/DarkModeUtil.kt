package com.maureen.wandevelop.util

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.maureen.wandevelop.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 * @author lianml
 * @date 2024/1/16
 */
object DarkModeUtil {
    private const val TAG = "DarkModeUtils"

    fun initFromPreference(context: Context) = runBlocking {
        val begin = System.currentTimeMillis()
        val mode = UserPrefUtil.getPreference(TAG)?.toInt() ?: getDarkMode(context)
        Log.d(TAG, "initFromPreference: dark mode $mode")
        AppCompatDelegate.setDefaultNightMode(mode)
        UserPrefUtil.setPreference(TAG, mode.toString())
        Log.d(TAG, "initFromPreference: spent ${System.currentTimeMillis() - begin}")
    }

    fun convertDarkModeName(context: Context, mode: Int): String {
        val darkModeNameMap = mapOf(
            AppCompatDelegate.MODE_NIGHT_NO to context.getString(R.string.promote_off),
            AppCompatDelegate.MODE_NIGHT_YES to context.getString(R.string.promote_on),
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM to context.getString(R.string.promote_dark_mode_follow_system)
        )
        return darkModeNameMap[mode] ?: context.getString(R.string.promote_dark_mode_follow_system)
    }

    fun getDarkMode(context: Context): Int {
        val nightModeFlags = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_YES
            Configuration.UI_MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_NO
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    }

    fun getDarkModeName(context: Context): String {
        val curMode = getDarkMode(context)
        Log.d(TAG, "getDarkModeName: $curMode")
        return convertDarkModeName(context, curMode)
    }

    fun getDarkModeFromPreference(context: Context): Flow<Int> {
        return UserPrefUtil.getPreferenceFlow(TAG).map {
            Log.d(TAG, "getDarkModeNameFromPreference: dark mode from preference $it")
            return@map if (it.isNullOrBlank()) {
                getDarkMode(context)
            } else {
                it.toInt()
            }
        }
    }

    suspend fun setDarkMode(mode: Int) {
        UserPrefUtil.setPreference(TAG, mode.toString())
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}