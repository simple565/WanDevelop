package com.maureen.wandevelop.feature.setting

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.maureen.wandevelop.MyApplication
import com.maureen.wandevelop.R
import com.maureen.wandevelop.core.BaseRepository
import com.maureen.wandevelop.entity.SettingItem
import com.maureen.wandevelop.entity.SettingType
import com.maureen.wandevelop.network.WanAndroidService
import com.maureen.wandevelop.util.DarkModeUtil
import com.maureen.wandevelop.util.UserPrefUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * @author lianml
 * @date 2025/3/13
 */
class SettingRepository : BaseRepository() {
    val defaultSettingList = mutableListOf<SettingItem>(
        SettingItem(
            name = R.string.prompt_clear_cache,
            icon = R.drawable.ic_arrow_right,
            type = SettingType.ACTION
        ),
        SettingItem(
            name = R.string.prompt_receive_notification,
            value = ""
        ),
        SettingItem(
            name = R.string.prompt_show_banner,
            value = ""
        ),
        SettingItem(
            name = R.string.prompt_show_sticky_top_article,
            value = ""
        ),
        SettingItem(
            name = R.string.prompt_show_hotkey,
            value = ""
        ),
        SettingItem(
            name = R.string.prompt_private_mode,
            value = ""
        ),
        SettingItem(
            name = R.string.prompt_dark_mode_follow_system,
            value = ""
        )
    )

    fun getSettingItems(context: Context): Flow<List<SettingItem>> {
        return combine(
            UserPrefUtil.getPreferenceFlow(UserPrefUtil.KEY_SHOW_BANNER, true.toString()),
            UserPrefUtil.getPreferenceFlow(
                UserPrefUtil.KEY_SHOW_STICK_TOP_ARTICLE,
                true.toString()
            ),
            UserPrefUtil.getPreferenceFlow(UserPrefUtil.KEY_SHOW_HOTKEY, true.toString()),
            UserPrefUtil.getPreferenceFlow(UserPrefUtil.KEY_PRIVATE_MODE, false.toString()),
            DarkModeUtil.getDarkModeFromPreference(context)
        ) { showBanner, showStickTopArticle, showHotkey, privateMode, darkMode ->
            return@combine defaultSettingList.flatMap {
                return@flatMap when (it.name) {
                    R.string.prompt_clear_cache -> listOf(it)
                    R.string.prompt_receive_notification -> listOf(it)
                    R.string.prompt_show_banner -> listOf(it.copy(value = showBanner.toString()))
                    R.string.prompt_show_sticky_top_article -> listOf(it.copy(value = showStickTopArticle.toString()))
                    R.string.prompt_show_hotkey -> listOf(it.copy(value = showHotkey.toString()))
                    R.string.prompt_private_mode -> listOf(it.copy(value = privateMode.toString()))
                    R.string.prompt_dark_mode_follow_system -> {
                        if (darkMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
                            listOf(it.copy(value = true.toString()))
                        } else {
                            listOf(
                                it.copy(value = false.toString()),
                                SettingItem(
                                    name = R.string.prompt_dark_mode,
                                    value = (darkMode == AppCompatDelegate.MODE_NIGHT_YES).toString(),
                                    type = SettingType.SWITCH
                                )
                            )
                        }
                    }

                    else -> listOf(it)
                }
            }
        }
    }

    suspend fun updateSetting(nameResId: Int, value: Boolean) {
        if (nameResId == R.string.prompt_dark_mode_follow_system) {
            val mode = if (value) {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            } else {
                DarkModeUtil.getDarkMode(MyApplication.instance.applicationContext)
            }
            DarkModeUtil.setDarkMode(mode)
            return
        }
        if (nameResId == R.string.prompt_dark_mode) {
            DarkModeUtil.setDarkMode(if (value) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            return
        }
        val key = when (nameResId) {
            R.string.prompt_show_banner -> UserPrefUtil.KEY_SHOW_BANNER
            R.string.prompt_show_sticky_top_article -> UserPrefUtil.KEY_SHOW_STICK_TOP_ARTICLE
            R.string.prompt_show_hotkey -> UserPrefUtil.KEY_SHOW_HOTKEY
            R.string.prompt_private_mode -> UserPrefUtil.KEY_PRIVATE_MODE
            else -> ""
        }
        if (key.isNotBlank()) {
            UserPrefUtil.setPreference(key, value.toString())
        }
    }

    suspend fun signOut() = requestSafely { WanAndroidService.instance.logout() }

    suspend fun clearProfileCache() {
        UserPrefUtil.setPreference(UserPrefUtil.KEY_USER_INFO, "")
        UserPrefUtil.setPreference(UserPrefUtil.KEY_USER_COLLECT_ID, "")
        UserPrefUtil.setPreference(UserPrefUtil.KEY_USER_UNREAD_MSG_COUNT, "")
        // TODO: 数据库删除
    }
}