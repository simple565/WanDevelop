package com.maureen.wandevelop.feature.discovery.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.maureen.wandevelop.common.entity.DataLoadState
import com.maureen.wandevelop.common.entity.Feed
import com.maureen.wandevelop.common.theme.WanDevelopTheme
import com.maureen.wandevelop.common.theme.WanDevelopTypography
import com.maureen.wandevelop.common.tooling.UiModePreviews
import com.maureen.wandevelop.entity.TagInfo

/**
 * @author lianml
 * @date 2025/3/9
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CourseListPage(
    loadState: DataLoadState<Feed>,
    onCourseClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberPullToRefreshState()
    PullToRefreshBox(
        isRefreshing = loadState.isLoading,
        onRefresh = { },
        modifier = modifier,
        state = state
    ) {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(items = loadState.dataList, key = { it.id }) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    CourseCard(
                        feed = it,
                        onCardClick = { onCourseClick(it.id) }
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(top = 10.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CourseCard(
    feed: Feed,
    modifier: Modifier = Modifier,
    onCardClick: (Feed) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onCardClick(feed)
            },
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(feed.coverUrl)
                .crossfade(500)
                .build(),
            modifier = Modifier
                .height(110.dp)
                .width(80.dp),
            contentDescription = "Cover",
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = AnnotatedString.fromHtml(feed.title),
                style = WanDevelopTypography.titleSmall,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = feed.author,
                style = WanDevelopTypography.labelMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
            Text(
                text = AnnotatedString.fromHtml(feed.description),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = WanDevelopTypography.labelMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }

    }
}


@UiModePreviews
@Composable
private fun CourseCardPreview() {
    val feed = Feed(
        id = 29464,
        title = "C语言入门教程_阮一峰",
        description = "C语言入门教程",
        url = "https=//www.wanandroid.com/blog/show/3732",
        coverUrl = "https://www.wanandroid.com/blogimgs/f1cb8d34-82c1-46f7-80fe-b899f56b69c1.png",
        isCollect = false,
        author = "阮一峰",
        publishData = "2025-01-28",
        tags = listOf(
            TagInfo(name = "广场", ""),
            TagInfo(name = "广场2", ""),
            TagInfo(name = "广场3", "")
        )
    )
    WanDevelopTheme {
        Column(
            Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            CourseCard(
                modifier = Modifier,
                feed = feed,
            )
        }
    }
}