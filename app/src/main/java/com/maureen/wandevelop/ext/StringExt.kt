package com.maureen.wandevelop.ext

import android.text.Spanned
import androidx.core.text.HtmlCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Function:
 * @author lianml
 * Create 2021-07-14
 */
fun String.displayHtml(): Spanned {
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

fun Long.timeStamp2Date(format: String = "yyyy-MM-dd HH:mm:ss"): String {
    return SimpleDateFormat(format, Locale.CHINA).format(Date(this))
}