package com.maureen.wandevelop.feature.discovery.course

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.maureen.wandevelop.R
import com.maureen.wandevelop.core.ext.clickable
import com.maureen.wandevelop.network.entity.ArticleInfo
import com.maureen.wandevelop.network.entity.SystemNodeInfo
import com.maureen.wandevelop.ui.composable.WanDevTopAppBar
import com.maureen.wandevelop.ui.theme.WanDevelopTheme
import com.maureen.wandevelop.ui.theme.WanDevelopTypography
import com.maureen.wandevelop.ui.tooling.UiModePreviews

/**
 * @author lianml
 * @date 2025/10/4
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CourseDetailScreen(
    parentId: Int,
    courseId: Int,
    onBackClick: () -> Unit,
    onFeedClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CourseDetailViewModel = viewModel()
) {
    LaunchedEffect(parentId, courseId) {
        viewModel.loadCourseDetail(parentId, courseId)
    }

    val courseState by viewModel.courseDetailState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            WanDevTopAppBar(
                title = "",
                onNavigationIconClick = onBackClick
            )
        },
        content = { padding ->
            PullToRefreshBox(
                isRefreshing = courseState.isLoading,
                onRefresh = { },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                state = rememberPullToRefreshState()
            ) {
                val chapterRowModifier = Modifier.fillMaxWidth()

                if (courseState.dataList.isEmpty()) {

                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        item {
                            CourseInfoRow(
                                course = courseState.dataList.first(),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        item {
                            Text(
                                text = stringResource(R.string.prompt_catalogue),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .fillMaxWidth()
                            )
                        }
                        if (courseState.dataList.first().children.isEmpty()) {
                            itemsIndexed(items = courseState.dataList.first().articleList, key = { index, item -> item.id }) { index, item ->
                                ChapterRow(
                                    index = index,
                                    articleInfo = item,
                                    modifier = chapterRowModifier,
                                    itemClick = { onFeedClick(it.link) }
                                )
                            }
                        } else {
                            items(items = courseState.dataList.first().children, key = { it.id }) {
                                ChapterGroupCard(
                                    systemNodeInfo = it,
                                    expanded = courseState.operatedIdSet.contains(it.id),
                                    toggleExpand = viewModel::toggleExpand,
                                    itemClick = { article -> onFeedClick(article.link) },
                                    modifier = chapterRowModifier
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun CourseInfoRow(course: SystemNodeInfo, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
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
                .padding(start = 12.dp)
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
                color = MaterialTheme.colorScheme.primary,
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
}

@Composable
private fun ChapterGroupCard(
    systemNodeInfo: SystemNodeInfo,
    expanded: Boolean,
    toggleExpand: (Int) -> Unit,
    itemClick: (ArticleInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { toggleExpand(systemNodeInfo.id) }
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = systemNodeInfo.name, style = MaterialTheme.typography.titleSmall)
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = ""
            )
        }
        val subRowModifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()

        if (expanded) {
            systemNodeInfo.articleList.forEachIndexed { index, info ->
                ChapterRow(
                    index = index,
                    articleInfo = info,
                    itemClick = itemClick,
                    modifier = subRowModifier
                )
            }
        }
    }
}

@Composable
private fun ChapterRow(
    articleInfo: ArticleInfo,
    itemClick: (ArticleInfo) -> Unit,
    modifier: Modifier = Modifier,
    index: Int = 0
) {
    Text(
        text = AnnotatedString.fromHtml("${index.plus(1)}.${articleInfo.title}"),
        modifier = modifier
            .clickable { itemClick(articleInfo) }
            .padding(vertical = 10.dp),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@UiModePreviews
@Composable
private fun CourseInfoRowPreview() {
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
            CourseInfoRow(
                modifier = Modifier,
                course = course,
            )
        }
    }
}

@UiModePreviews
@Composable
private fun ChapterRowPreview() {
    val article = ArticleInfo(
        adminAdd = false,
        apkLink = "",
        audit = 1,
        author = "阮一峰",
        canEdit = false,
        chapterId = 549,
        chapterName = "HTML 教程_阮一峰",
        collect = false,
        courseId = 13,
        desc = "",
        descMd = "",
        envelopePic = "",
        fresh = false,
        host = "",
        id = 21894,
        isAdminAdd = false,
        link = "https://www.wanandroid.com/blog/show/3214",
        niceDate = "2022-03-22 15:03",
        niceShareDate = "2022-03-22 15:03",
        origin = "",
        prefix = "",
        projectLink = "",
        publishTime = 1647932634000,
        realSuperChapterId = 547,
        selfVisible = 0,
        shareDate = 1647932593000,
        shareUser = "",
        superChapterId = 548,
        superChapterName = "教程",
        title = "其他标签",
        type = 0,
        userId = -1,
        visible = 0,
        zan = 0
    )
    WanDevelopTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            ChapterRow(
                articleInfo = article,
                itemClick = {},
                modifier = Modifier,
            )
        }
    }
}