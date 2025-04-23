package com.maureen.wandevelop.feature.discovery.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maureen.wandevelop.common.entity.DataLoadState
import com.maureen.wandevelop.entity.ArticleInfo
import com.maureen.wandevelop.entity.SystemNodeInfo

/**
 * 学习路线列表
 * @date 2025/3/11
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RouteListPage(
    loadState: DataLoadState<SystemNodeInfo>,
    toggleExpand: (SystemNodeInfo) -> Unit,
    onRouteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberPullToRefreshState()
    PullToRefreshBox(
        isRefreshing = loadState.isLoading,
        onRefresh = { },
        modifier = modifier,
        state = state
    ) {
        LazyColumn(
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceBright,
                    shape = MaterialTheme.shapes.large
                ),
        ) {
            items(items = loadState.dataList, key = { it.id }) {
                ParentNodeCard(
                    nodeInfo = it,
                    expandedIdSet = loadState.operatedIdSet,
                    toggleExpand = toggleExpand,
                    itemClick = { onRouteClick(it.link) }
                )
            }
        }
    }
}

@Composable
private fun ParentNodeCard(
    nodeInfo: SystemNodeInfo,
    expandedIdSet: Set<Long>,
    toggleExpand: (SystemNodeInfo) -> Unit,
    itemClick: (ArticleInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    toggleExpand(nodeInfo)
                }
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = nodeInfo.name, style = MaterialTheme.typography.titleSmall)
            Icon(
                imageVector = if (expandedIdSet.contains(nodeInfo.id)) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = ""
            )
        }
        if (expandedIdSet.contains(nodeInfo.id)) {
            nodeInfo.children.forEach {
                Text(
                    text = it.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            //itemClick(it)
                        }
                        .padding(10.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}