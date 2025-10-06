package com.maureen.wandevelop.feature.discovery.course

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
import com.maureen.wandevelop.core.entity.DataLoadState
import com.maureen.wandevelop.network.entity.SystemNodeInfo
import com.maureen.wandevelop.ui.theme.WanDevelopTheme
import com.maureen.wandevelop.ui.theme.WanDevelopTypography
import com.maureen.wandevelop.ui.tooling.UiModePreviews

/**
 * @author lianml
 * @date 2025/3/9
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CourseListPage(
    loadState: DataLoadState<SystemNodeInfo>,
    onCourseClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberPullToRefreshState()
    PullToRefreshBox(
        isRefreshing = loadState.isLoading,
        onRefresh = { },
        modifier = modifier,
        state = state
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = loadState.dataList, key = { it.id }) {
                CourseCard(
                    course = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onCourseClick(it.parentChapterId, it.id)
                        }.padding(horizontal = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun CourseCard(
    course: SystemNodeInfo,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.padding(vertical = 16.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(course.cover)
                    .crossfade(500)
                    .build(),
                modifier = Modifier
                    .height(120.dp)
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
                    text = AnnotatedString.fromHtml(course.name),
                    style = WanDevelopTypography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = course.author,
                    style = WanDevelopTypography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                Text(
                    text = AnnotatedString.fromHtml(course.desc),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = WanDevelopTypography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
    }
}


@UiModePreviews
@Composable
private fun CourseCardPreview() {
    val course = SystemNodeInfo(
        id = 548,
        courseId = 13,
        name = "C语言入门教程_阮一峰",
        desc = "C语言入门教程",
        cover = "https://www.wanandroid.com/blogimgs/f1cb8d34-82c1-46f7-80fe-b899f56b69c1.png",
        author = "阮一峰",
        articleList = emptyList(),
        children = emptyList(),
        license = "知识共享 署名-相同方式共享 3.0协议",
        licenseLink = "https://creativecommons.org/licenses/by-sa/3.0/deed.zh",
        order = 270000,
        parentChapterId = 547,
        type = 0,
        userControlSetTop = false,
        visible = 1
    )
    WanDevelopTheme {
        Column(
            Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(vertical = 10.dp)
        ) {
            CourseCard(
                modifier = Modifier.padding(12.dp),
                course = course,
            )
        }
    }
}