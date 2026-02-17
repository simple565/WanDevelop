package com.maureen.wandevelop.feature.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.maureen.wandevelop.network.entity.PopularSectionItem
import com.maureen.wandevelop.ui.theme.WanDevelopTheme
import com.maureen.wandevelop.ui.tooling.UiModePreviews

/**
 * @author Lian
 * @date 2026/2/14
 */
@Composable
fun MostPopularSectionRow(
    sections: List<Pair<String, List<PopularSectionItem>>>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(count = sections.size, key = { sections[it].first }) {
            SectionColumn(
                infoPair = sections[it], modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
private fun SectionColumn(
    infoPair: Pair<String, List<PopularSectionItem>>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1F),
                shape = MaterialTheme.shapes.medium
            )
            .background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5F),
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Text(
            text = infoPair.first,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1F),
                    shape = MaterialTheme.shapes.medium
                )
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            infoPair.second.forEach {
                Text(
                    text = it.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@UiModePreviews
@Composable
private fun SectionColumnPreview() {
    WanDevelopTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            SectionColumn(
                infoPair = Pair(
                    "最受欢迎板块", listOf(
                        PopularSectionItem(
                            "每日一问 | Dialog 的构造方法的 context 必须传入 Activity吗？",
                            ""
                        ),
                        PopularSectionItem(
                            "每日一问 | Dialog 的构造方法的 context 必须传入 Activity吗？",
                            ""
                        ),
                        PopularSectionItem(
                            "每日一问 | Dialog 的构造方法的 context 必须传入 Activity吗？",
                            ""
                        )
                    )
                ), modifier = Modifier
                    .width(300.dp)
                    .height(160.dp)
            )
        }
    }
}