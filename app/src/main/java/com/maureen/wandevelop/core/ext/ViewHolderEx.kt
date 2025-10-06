package com.maureen.wandevelop.core.ext

import android.content.Context
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * @author lianml
 * @date 2024/5/26
 */
val ViewHolder.context: Context
    get() {
        return this.itemView.context
    }