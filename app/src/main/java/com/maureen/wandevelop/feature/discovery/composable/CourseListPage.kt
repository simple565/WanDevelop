package com.maureen.wandevelop.feature.discovery.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.maureen.wandevelop.common.entity.DataLoadState
import com.maureen.wandevelop.common.entity.Feed
import com.maureen.wandevelop.common.theme.WanDevelopTheme
import com.maureen.wandevelop.common.theme.WanDevelopTypography
import com.maureen.wandevelop.entity.TagInfo

/**
 * @author lianml
 * @date 2025/3/9
 */
@Composable
internal fun CourseListPage(
    loadState: DataLoadState<Feed>,
    modifier: Modifier = Modifier
) {
    if (loadState.isLoading) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
        return
    }
    if (loadState.dataList.isEmpty()) {

    } else {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(items = loadState.dataList, key = { it.id }) {
                CourseCard(
                    feed = it,
                    onCardClick = {}
                )
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
            .padding(vertical = 2.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(10.dp)
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
        Spacer(modifier = Modifier.size(10.dp))
        Column {
            Text(text = feed.title, style = WanDevelopTypography.titleMedium)
            Spacer(modifier = Modifier.size(10.dp))
            Text(text = feed.author, style = WanDevelopTypography.labelMedium)
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = feed.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = WanDevelopTypography.labelMedium
            )
        }

    }
}

@Composable
@Preview
private fun CourseCardPreview() {
    val feed = Feed(
        id = 29464,
        title = "【HenCoder Android 开发进阶】自定义 View 1-7：属性动画（进阶篇）",
        description = "【HenCoder Android 开发进阶】自定义 View 1-7：属性动画（进阶篇）",
        url = "https=//www.wanandroid.com/blog/show/3732",
        coverUrl = "https://www.wanandroid.com/blogimgs/f1cb8d34-82c1-46f7-80fe-b899f56b69c1.png",
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
        CourseCard(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface),
            feed = feed,
        )
    }
}