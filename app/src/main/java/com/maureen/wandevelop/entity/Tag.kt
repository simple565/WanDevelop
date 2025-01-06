package com.maureen.wandevelop.entity

import android.content.Context
import com.maureen.wandevelop.R
import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val name: String,
    val url: String
) {
    fun getTextColorResId(context: Context): Int {
        return when (this.name) {
            context.getString(R.string.newest), context.getString(R.string.top) -> R.color.red_700
            else -> R.color.textBody
        }
    }

    fun getBackgroundDrawableResId(context: Context): Int {
        return when (this.name) {
            context.getString(R.string.newest), context.getString(R.string.top) -> R.drawable.bg_solid_red_50_radius_6
            else -> R.drawable.bg_tag
        }
    }
}