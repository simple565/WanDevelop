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
    val name: String,
    val icon: Int? = null,
    var value: String? = null,
    val warn: Boolean = false,
    val action: (() -> Unit)? = null
) {
    companion object {
        val EMPTY = SettingItem(SettingType.EMPTY, "")
    }
}

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

