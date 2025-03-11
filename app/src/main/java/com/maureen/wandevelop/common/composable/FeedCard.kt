package com.maureen.wandevelop.common.composable

import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.maureen.wandevelop.R
import com.maureen.wandevelop.common.entity.Feed
import com.maureen.wandevelop.common.theme.WanDevelopTheme
import com.maureen.wandevelop.common.theme.WanDevelopTypography
import com.maureen.wandevelop.entity.TagInfo
import com.maureen.wandevelop.ext.displayHtml

/**
 * 文章相关Composable
 * @author lianml
 * @date 2025/2/4
 */
@Composable
fun FeedPagingColumn(
    pagingItems: LazyPagingItems<Feed>,
    modifier: Modifier = Modifier,
    showCollectButton: Boolean = false,
    collectedFeedIdSet: Set<Long> = emptySet(),
    onCollectClick: (Feed) -> Unit = {},
    showMoreButton: Boolean = false,
    onMoreClick: (Feed) -> Unit = {},
    onItemClick: (Feed) -> Unit = {}
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(
            count = pagingItems.itemCount,
            key = pagingItems.itemKey { it.id }
        ) { index ->
            val feed = pagingItems[index] ?: return@items
            FeedCard(
                feed = feed,
                modifier = Modifier.padding(10.dp),
                showCollectButton = showCollectButton,
                isCollect = collectedFeedIdSet.contains(feed.id),
                onCollectClick = onCollectClick,
                showMoreButton = showMoreButton,
                onMoreClick = onMoreClick,
                onCardClick = onItemClick
            )
        }
    }
}

@Composable
fun FeedCard(
    feed: Feed,
    modifier: Modifier = Modifier,
    showCollectButton: Boolean = false,
    isCollect: Boolean = false,
    onCollectClick: (Feed) -> Unit = {},
    showMoreButton: Boolean = false,
    onMoreClick: (Feed) -> Unit = {},
    onCardClick: (Feed) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(vertical = 2.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .clickable { onCardClick(feed) }
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AndroidView(
                factory = {
                    TextView(it).apply {
                        this.text = feed.title.displayHtml()
                    }
                },
                modifier = Modifier.weight(weight = if (showCollectButton) 0.9F else 1F),
            )
            if (showCollectButton) {
                IconButton(
                    onClick = { onCollectClick(feed) },
                    modifier = Modifier.weight(weight = 0.1F),
                    content = {
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                if (isCollect) R.drawable.ic_bookmark_activated else R.drawable.ic_bookmark_normal
                            ),
                            modifier = Modifier,
                            contentDescription = ""
                        )
                    }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.primary)
            )
            Text(text = feed.author, style = MaterialTheme.typography.labelSmall)
            Text(
                "", modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.primary)
            )
            Text(text = feed.publishData, style = MaterialTheme.typography.labelSmall)
        }

        if (feed.tags.isNotEmpty() || showMoreButton) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (feed.tags.isNotEmpty()) {
                    TagRow(modifier = Modifier.weight(weight = 0.9F), tagList = feed.tags)
                }
                if (showMoreButton) {
                    IconButton(onClick = {
                        onMoreClick(feed)
                    }, modifier = Modifier.size(32.dp)) {
                        Icon(
                            Icons.Default.MoreVert,
                            modifier = Modifier.size(20.dp),
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TagRow(tagList: List<TagInfo>, modifier: Modifier = Modifier) {
    val tagModifier = modifier
        .background(
            color = MaterialTheme.colorScheme.inverseOnSurface,
            shape = MaterialTheme.shapes.large
        )
        .padding(6.dp)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        items(items = tagList, key = { tag -> tag.name }) {
            Text(
                text = it.name,
                modifier = tagModifier,
                color = MaterialTheme.colorScheme.inverseSurface,
                style = WanDevelopTypography.labelSmall,
            )
        }
    }
}

@Composable
@Preview
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
    WanDevelopTheme(darkThemeOn = false) {
        FeedCard(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface),
            feed = feed,
            showCollectButton = true,
            onCollectClick = {},
            showMoreButton = true,
            onMoreClick = {}
        )
    }
}