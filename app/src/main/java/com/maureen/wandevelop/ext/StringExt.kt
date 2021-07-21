package com.maureen.wandevelop.ext

import android.text.Spanned
import androidx.core.text.HtmlCompat

/**
 * Function:
 * @author lianml
 * Create 2021-07-14
 */
fun String.fromHtml(): Spanned {
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
}