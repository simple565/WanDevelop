package com.maureen.wandevelop.common

import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.maureen.wandevelop.R
import com.maureen.wandevelop.common.theme.WanDevelopTheme
import com.maureen.wandevelop.common.theme.WanDevelopTypography
import com.maureen.wandevelop.entity.Article
import com.maureen.wandevelop.entity.Tag
import com.maureen.wandevelop.ext.displayAuthor
import com.maureen.wandevelop.ext.displayHtml
import com.maureen.wandevelop.ext.displayPublishData

/**
 * 文章相关Composable
 * @author lianml
 * @date 2025/2/4
 */
@Composable
fun ArticleCard(
    article: Article,
    onCollectClick: (Article) -> Unit,
    onMoreClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
    onItemClick: (Article) -> Unit = {}
) {
    ElevatedCard(
        modifier = modifier.clickable { onItemClick(article) },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AndroidView(
                    factory = {
                        TextView(it).apply {
                            this.text = article.title.displayHtml()
                        }
                    },
                    modifier = Modifier.weight(weight = 0.9F),
                )
                IconButton(onClick = {
                    onCollectClick(article)
                }, modifier = Modifier.weight(weight = 0.1F)) {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            if (article.collect) R.drawable.ic_bookmark_activated else R.drawable.ic_bookmark_normal
                        ),
                        modifier = Modifier.size(20.dp),
                        contentDescription = ""
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
                Text(
                    "",
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )
                Text(text = article.displayAuthor, style = MaterialTheme.typography.labelSmall)
                Text(
                    "", modifier = Modifier
                        .size(4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = CircleShape
                        )
                )
                Text(text = article.displayPublishData, style = MaterialTheme.typography.labelSmall)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TagRow(modifier = Modifier.weight(weight = 0.9F), tagList = article.tags)
                IconButton(onClick = {
                    onMoreClick(article)
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

@Composable
fun TagRow(tagList: List<Tag>, modifier: Modifier = Modifier) {
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
fun ArticleItemPreview() {
    val article = Article(
        adminAdd = false,
        apkLink = "",
        audit = 1,
        author = "张鸿洋",
        canEdit = false,
        chapterId = 543,
        chapterName = "Android技术周报",
        collect = false,
        courseId = 13,
        desc = "",
        descMd = "",
        envelopePic = "",
        fresh = false,
        host = "",
        id = 29464,
        isAdminAdd = false,
        link = "https=//www.wanandroid.com/blog/show/3732",
        niceDate = "2025-01-28 00:00",
        niceShareDate = "2025-01-28 00:10",
        origin = "",
        prefix = "",
        projectLink = "",
        publishTime = 1737993600000,
        realSuperChapterId = 542,
        selfVisible = 0,
        shareDate = 1737994200000,
        shareUser = "",
        superChapterId = 543,
        superChapterName = "技术周报",
        tags = listOf(Tag(name = "广场", ""), Tag(name = "广场2", ""), Tag(name = "广场3", "")),
        title = "Android 技术周刊xxxxxxx （2025-01-21 ~ 2025-01-28）",
        type = 0,
        userId = -1,
        visible = 1,
        zan = 0
    )
    WanDevelopTheme(darkThemeOn = false) {
        ArticleCard(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface),
            article = article,
            onCollectClick = {},
            onMoreClick = {}
        )
    }
}