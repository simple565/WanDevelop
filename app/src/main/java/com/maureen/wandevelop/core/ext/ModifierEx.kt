package com.maureen.wandevelop.core.ext

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier

/**
 * @author lianml
 * @date 2025/10/8
 */
fun Modifier.clickable(onClick: () -> Unit) = this.then(
    Modifier.clickable(
        interactionSource = null,
        indication = null,
        onClick = onClick
    )
)