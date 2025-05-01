package com.maureen.wandevelop.main

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.maureen.wandevelop.main.ui.WanDevelopApp
import com.maureen.wandevelop.main.ui.rememberWanDevAppState
import com.maureen.wandevelop.ui.theme.WanDevelopTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 沉浸式导航栏适配小米手机
        if (Build.MANUFACTURER.equals("xiaomi", true)) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
        enableEdgeToEdge()
        setContent {
            val appState = rememberWanDevAppState()
            WanDevelopTheme {
                WanDevelopApp(
                    appState = appState
                )
            }
        }
    }
}