package com.maureen.wandevelop.ui.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.maureen.wandevelop.R
import com.maureen.wandevelop.core.entity.Feed
import com.maureen.wandevelop.network.entity.TagInfo
import com.maureen.wandevelop.ui.theme.WanDevelopTheme
import com.maureen.wandevelop.ui.theme.WanDevelopTypography
import com.maureen.wandevelop.ui.tooling.UiModePreviews

/**
 * Feed流页面（下拉刷新，上拉加载更多）
 */
@Composable
fun FeedPullToRefreshBox(
    pagingItems: LazyPagingItems<Feed>,
    modifier: Modifier = Modifier,
    header: @Composable (() -> Unit)? = null,
    collectedFeedIdSet: Set<Int> = emptySet(),
    showCollectButton: Boolean = true,
    showMoreButton: Boolean = true,
    onItemClick: (Feed) -> Unit = {},
    onItemLongClick: (Feed) -> Unit = {},
    onMoreClick: (Feed) -> Unit = {},
    toggleCollect: (Feed, Boolean) -> Unit = { _, _ -> },
    noMoreDataHint: String = stringResource(R.string.load_no_more_data),
    onNoMoreDataHintClick: () -> Unit = {}
) {
    val state = rememberPullToRefreshState()
    val scrollState = rememberLazyListState()
    PullToRefreshBox(
        isRefreshing = pagingItems.loadState.refresh == LoadState.Loading,
        onRefresh = { pagingItems.refresh() },
        modifier = modifier,
        state = state
    ) {
        FeedPagingColumn(
            pagingItems = pagingItems,
            modifier = Modifier.fillMaxSize(),
            header = header,
            state = scrollState,
            collectedFeedIdSet = collectedFeedIdSet,
            showCollectButton = showCollectButton,
            showMoreButton = showMoreButton,
            onItemClick = onItemClick,
            onItemLongClick = onItemLongClick,
            toggleCollect = toggleCollect,
            onMoreClick = onMoreClick,
            noMoreDataHint = noMoreDataHint,
            onNoMoreDataHintClick = onNoMoreDataHintClick
        )
    }
}

/**
 * Feed列表
 */
@Composable
fun FeedPagingColumn(
    pagingItems: LazyPagingItems<Feed>,
    modifier: Modifier = Modifier,
    header: @Composable (() -> Unit)? = null,
    state: LazyListState = rememberLazyListState(),
    collectedFeedIdSet: Set<Int> = emptySet(),
    showCollectButton: Boolean = false,
    showMoreButton: Boolean = false,
    onItemClick: (Feed) -> Unit = {},
    onItemLongClick: (Feed) -> Unit = {},
    toggleCollect: (Feed, Boolean) -> Unit = { _, _ -> },
    onMoreClick: (Feed) -> Unit = {},
    noMoreDataHint: String = stringResource(R.string.load_no_more_data),
    onNoMoreDataHintClick: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        if (null != header) {
            item {
                header()
            }
        }
        items(
            count = pagingItems.itemCount,
            key = pagingItems.itemKey { it.id }
        ) { index ->
            val feed = pagingItems[index] ?: return@items
            FeedCard(
                feed = feed,
                modifier = Modifier,
                showCollectButton = showCollectButton,
                isCollect = collectedFeedIdSet.contains(feed.id),
                toggleCollect = toggleCollect,
                showMoreButton = showMoreButton,
                onMoreClick = onMoreClick,
                onCardClick = onItemClick,
                onCardLongClick = onItemLongClick
            )
        }
        pagingItems.loadState.append.also {
            if (it is LoadState.NotLoading && it.endOfPaginationReached) {
                item {
                    Text(
                        text = noMoreDataHint,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth()
                            .clickable(onClick = onNoMoreDataHintClick)
                            .padding(vertical = 10.dp)
                    )
                }
            } else if (it is LoadState.Loading) {
                item {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            } else {
                val errorMsg =
                    (pagingItems.loadState.append as? LoadState.Error)?.error?.message ?: ""
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeedCard(
    feed: Feed,
    modifier: Modifier = Modifier,
    showCollectButton: Boolean = false,
    isCollect: Boolean = false,
    toggleCollect: (Feed, Boolean) -> Unit = { _, _ -> },
    showMoreButton: Boolean = false,
    onMoreClick: (Feed) -> Unit = {},
    onCardClick: (Feed) -> Unit = {},
    onCardLongClick: (Feed) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 100.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceBright,
                shape = MaterialTheme.shapes.medium
            )
            .clip(shape = MaterialTheme.shapes.medium)
            .combinedClickable(
                onLongClick = { onCardLongClick(feed) },
                onClick = { onCardClick(feed) },
            )
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = AnnotatedString.fromHtml(feed.title),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = if (showCollectButton) 28.dp else 0.dp),
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (showCollectButton) {
                IconButton(
                    onClick = { toggleCollect(feed, isCollect.not()) },
                    modifier = Modifier
                        .size(28.dp)
                        .align(alignment = Alignment.CenterEnd),
                    content = {
                        Icon(
                            painter = painterResource(
                                if (isCollect) R.drawable.ic_bookmark_activated else R.drawable.ic_bookmark_normal
                            ),
                            tint = if (isCollect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inverseSurface,
                            modifier = Modifier,
                            contentDescription = ""
                        )
                    }
                )
            }
        }

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (feed.author.isNotBlank()) {
                    Spacer(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(color = MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        text = feed.author,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                if (feed.publishData.isNotBlank()) {
                    Spacer(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(color = MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        text = feed.publishData,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            if (feed.tags.isNotEmpty() || showMoreButton) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (feed.tags.isNotEmpty()) {
                        TagRow(
                            tagList = feed.tags,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = if (showMoreButton) 28.dp else 0.dp)
                        )
                    }
                    if (showMoreButton) {
                        IconButton(
                            onClick = { onMoreClick(feed) },
                            modifier = Modifier
                                .size(28.dp)
                                .align(alignment = Alignment.CenterEnd),
                            content = {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    tint = MaterialTheme.colorScheme.inverseSurface,
                                    modifier = Modifier,
                                    contentDescription = ""
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TagRow(tagList: List<TagInfo>, modifier: Modifier = Modifier) {
    val tagModifier = Modifier
        .background(
            color = MaterialTheme.colorScheme.inverseOnSurface,
            shape = MaterialTheme.shapes.medium
        )
        .padding(6.dp)
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp, alignment = Alignment.Start)
    ) {
        items(items = tagList, key = { tag -> tag.name }) {
            Text(
                text = it.name,
                modifier = tagModifier,
                style = WanDevelopTypography.labelSmall,
                color = MaterialTheme.colorScheme.inverseSurface,
            )
        }
    }
}

@UiModePreviews
@Composable
fun FeedCardPreview() {
    val feed = Feed(
        id = 29464,
        title = "【HenCoder Android 开发进阶】自定义 View 1-7：属性动画（进阶篇）",
        url = "https=//www.wanandroid.com/blog/show/3732",
        isCollect = false,
        author = "玩Android",
        publishData = "2025-01-28",
        tags = listOf(
            TagInfo(name = "广场", ""),
            TagInfo(name = "广场2", ""),
            TagInfo(name = "广场3", "")
        )
    )
    WanDevelopTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            FeedCard(
                modifier = Modifier,
                feed = feed,
                isCollect = false,
                showCollectButton = true,
                toggleCollect = { _, _ -> },
                showMoreButton = true,
                onMoreClick = {}
            )
        }
    }
}