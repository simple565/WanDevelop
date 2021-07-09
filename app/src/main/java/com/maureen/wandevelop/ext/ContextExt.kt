package com.maureen.wandevelop.ext

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Function:
 * Date:   2021/7/9
 * @author lianml
 */
fun Context.showToast(message: CharSequence) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}