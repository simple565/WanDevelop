package com.maureen.wandevelop.entity

import android.view.View
import androidx.annotation.IntDef

/**
 * 设置项
 * @author lianml
 * @date 2023/12/25
 */
data class SettingItem(
    @SettingType val type: Int = SettingType.ACTION,
    val iconResId: Int,
    val name: String,
    val value: String? = null,
    val clickAction: ((View) -> Unit)? = null
)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@MustBeDocumented
@IntDef(
    SettingType.TITLE,
    SettingType.ACTION,
    SettingType.ROUTE,
    SettingType.ACTION_WITH_TEXT,
    SettingType.SWITCH
)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation
class SettingType {
    companion object {
        const val TITLE = 0x2
        const val ACTION = 0x3
        const val SWITCH = 0x4
        const val ROUTE = 0x5
        const val ACTION_WITH_TEXT = 0x6
    }
}

