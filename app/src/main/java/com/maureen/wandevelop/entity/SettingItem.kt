package com.maureen.wandevelop.entity

import androidx.annotation.IntDef
import androidx.annotation.StringRes

/**
 * 设置项
 * @date 2023/12/25
 */
data class SettingItem(
    @param:SettingType val type: Int = SettingType.SWITCH,
    @param:StringRes val name: Int,
    val icon: Int? = null,
    val value: String? = null,
    val warn: Boolean = false,
    val action: (() -> Unit)? = null
)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@MustBeDocumented
@IntDef(
    SettingType.EMPTY,
    SettingType.ACTION,
    SettingType.ROUTE,
    SettingType.SWITCH
)
@Retention(AnnotationRetention.SOURCE)
annotation
class SettingType {
    companion object {
        const val EMPTY = 0x1
        const val ACTION = 0x2
        const val SWITCH = 0x3
        const val ROUTE = 0x4
    }
}
