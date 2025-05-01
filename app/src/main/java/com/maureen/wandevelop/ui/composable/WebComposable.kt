package com.maureen.wandevelop.ui.composable

import android.graphics.Bitmap
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/**
 * @author lianml
 * @date 2025/5/1
 */
@Composable
fun WebComposable(
    url: String,
    modifier: Modifier = Modifier,
    onPageLoadStarted: (() -> Unit)? =  null,
    onPageLoadFinished: (() -> Unit)? =  null,
    onPageLoadProgress: ((Int) -> Unit)? = null
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                // 启用 DOM 存储
                settings.domStorageEnabled = true
                // 适应屏幕大小
                settings.loadWithOverviewMode = true
                // 启用广泛视图模式
                settings.useWideViewPort = true
                settings.javaScriptCanOpenWindowsAutomatically = true
                settings.allowFileAccess = true
                settings.setGeolocationEnabled(true)
                settings.cacheMode = WebSettings.LOAD_DEFAULT
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
                webViewClient = object :WebViewClient(){
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        onPageLoadStarted?.invoke()
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        onPageLoadFinished?.invoke()
                    }
                }
                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        onPageLoadProgress?.invoke(newProgress)
                    }
                }
                loadUrl(url)
            }
        },
        modifier = modifier
    )
}