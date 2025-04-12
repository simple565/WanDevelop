package com.maureen.wandevelop.feature.profile.notification

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.maureen.wandevelop.R
import com.maureen.wandevelop.common.composable.WanDevTpoAppBar
import com.maureen.wandevelop.entity.MessageInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NotificationScreen(
    unreadMsgCount: Int,
    onBackClick: () -> Unit,
    onMessageClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel = viewModel()
) {
    LaunchedEffect(unreadMsgCount) {
        viewModel.showReadMsgList(unreadMsgCount)
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            WanDevTpoAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.nav_notification),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = ""
                            )
                        }
                    )
                }
            )
        },
        content = { padding ->
            val state = rememberPullToRefreshState()
            val pagingItems = viewModel.messageListFlow.collectAsLazyPagingItems()

            PullToRefreshBox(
                isRefreshing = pagingItems.loadState.refresh == LoadState.Loading,
                onRefresh = {
                    // TODO: 会崩溃暂时注释
                    /*pagingItems.refresh()*/
                },
                modifier = Modifier.padding(top = padding.calculateTopPadding()),
                state = state
            ) {
                LazyColumn(modifier = modifier.fillMaxSize()) {
                    items(
                        count = pagingItems.itemCount,
                        key = pagingItems.itemKey { it.id }
                    ) { index ->
                        val message = pagingItems[index] ?: return@items
                        MessageCard(
                            message = message,
                            onMessageClick = onMessageClick
                        )
                    }
                    pagingItems.loadState.append.also {
                        if (it is LoadState.NotLoading && it.endOfPaginationReached) {
                            item {
                                TextButton(
                                    onClick = {
                                        if (pagingItems[0]?.isRead == 0) {
                                            viewModel.showReadMsgList()

                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(vertical = 10.dp),
                                    content = {
                                        Text(
                                            text = stringResource(R.string.load_no_more_data),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                )
                            }
                        } else if (it is LoadState.Loading) {
                            item {
                                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                            }
                        } else {
                            val errorMsg =
                                (pagingItems.loadState.append as? LoadState.Error)?.error?.message
                                    ?: ""
                            if (errorMsg.isNotBlank()) {
                                item {
                                    Text(
                                        text = errorMsg,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentWidth()
                                            .padding(vertical = 10.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun MessageCard(
    message: MessageInfo,
    onMessageClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable {
                onMessageClick(message.fullLink)
            }
            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message.fromUser,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = message.niceDate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = message.title,
            modifier = Modifier.padding(top = 6.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = message.message,
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .padding(4.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
@Composable
private fun MessageCardPreview() {
    val message = MessageInfo(
        category = 1,
        date = 1716777886000,
        fromUser = "图你怀中安稳",
        fromUserId = 41223,
        fullLink = "https://wanandroid.com/wenda/show/8857",
        id = 1119791,
        isRead = 1,
        link = "/wenda/show/8857",
        message = "使用Shading, Shading可以将依赖库重新打包到一个不同的命名空间中.使用工具如shadowJar插件来实现这个功能。配置如下：1. 添加shadowJar插件依赖在SDK的build.gradle文件中，添加shadow插件依赖：plugins     id 'com.github.johnrengelman.shadow' version '7.1.0'...",
        niceDate = "2024-05-27 10:44",
        tag = "评论回复",
        title = "回复了@猪猪太",
        userId = 31041
    )
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            MessageCard(
                message = message,
                onMessageClick = {}
            )
        }
    }
}