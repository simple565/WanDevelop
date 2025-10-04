package com.maureen.wandevelop.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import com.maureen.wandevelop.main.ui.WanDevelopApp
import com.maureen.wandevelop.main.ui.rememberWanDevAppState
import com.maureen.wandevelop.ui.theme.WanDevelopTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 沉浸式导航栏适配小米手机
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets -> insets }
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