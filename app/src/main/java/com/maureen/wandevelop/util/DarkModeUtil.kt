package com.maureen.wandevelop.util

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.maureen.wandevelop.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author lianml
 * @date 2024/1/16
 */
object DarkModeUtil {
    private const val TAG = "DarkModeUtils"

    suspend fun initFromPreference(context: Context) {
        val begin = System.currentTimeMillis()
        val preference = withContext(Dispatchers.IO) {
            UserPrefUtil.getPreference(context)?.darkMode ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        Log.d(TAG, "init: dark mode $preference")
        AppCompatDelegate.setDefaultNightMode(preference)
        Log.d(TAG, "init: spent ${System.currentTimeMillis() - begin}")
    }

    fun convertDarkModeName(context: Context, mode: Int): String {
        val darkModeNameMap = mapOf(
            AppCompatDelegate.MODE_NIGHT_NO to context.getString(R.string.promote_off),
            AppCompatDelegate.MODE_NIGHT_YES to context.getString(R.string.promote_on),
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM to context.getString(R.string.promote_dark_mode_follow_system)
        )
        return darkModeNameMap[mode] ?: context.getString(R.string.promote_dark_mode_follow_system)
    }

    fun getDarkModeName(context: Context): String {
        val curMode = AppCompatDelegate.getDefaultNightMode()
        if (curMode == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED) {
            return if (isSystemDarkModeOn(context)) {
                convertDarkModeName(context, AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                convertDarkModeName(context, AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        Log.d(TAG, "getDarkModeName: $curMode")
        return convertDarkModeName(context, curMode)
    }

    private fun isSystemDarkModeOn(context: Context): Boolean {
        val configuration = context.resources.configuration
        val uiMode = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return uiMode == Configuration.UI_MODE_NIGHT_YES
    }
}