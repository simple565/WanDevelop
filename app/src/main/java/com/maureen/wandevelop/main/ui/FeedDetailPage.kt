package com.maureen.wandevelop.main.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maureen.wandevelop.ui.composable.WebComposable

/**
 * 文章页面
 * @date 2025/5/1
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedDetailPage(
    url: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLoading = remember { mutableStateOf(true) }
    val loadProgress = remember { mutableIntStateOf(0) }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            ToolButtons(
                onBackClick = onBackClick,
                progress = loadProgress.intValue
            )
        },
        content = { padding ->
            WebComposable(
                url = url,
                onPageLoadStarted = { isLoading.value = true },
                onPageLoadFinished = { isLoading.value = false },
                onPageLoadProgress = { loadProgress.intValue = it },
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
                    .padding(padding)
            )
        }
    )
}

@Composable
private fun ToolButtons(
    progress: Int,
    onBackClick: () -> Unit
) {

    Box(modifier = Modifier.size(60.dp)) {
        AnimatedVisibility(
            enter = fadeIn(),
            exit = fadeOut(),
            visible = progress < 100
        ) {
            CircularProgressIndicator(
                progress = { progress / 100F },
                modifier = Modifier.fillMaxSize()
            )
        }
        FloatingActionButton(
            onClick = onBackClick,
            shape = CircleShape,
            content = {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null
                )
            },
            modifier = Modifier.align(alignment = Alignment.Center)
        )
    }
}