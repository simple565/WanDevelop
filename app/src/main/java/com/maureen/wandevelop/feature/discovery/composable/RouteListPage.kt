package com.maureen.wandevelop.feature.discovery.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maureen.wandevelop.common.entity.DataLoadState
import com.maureen.wandevelop.common.theme.WanDevelopShapes
import com.maureen.wandevelop.entity.SystemNodeInfo

/**
 * 学习路线列表
 * @date 2025/3/11
 */
@Composable
internal fun RouteListPage(
    loadState: DataLoadState<SystemNodeInfo>,
    toggleExpand: (SystemNodeInfo) -> Unit,
    itemClick: (SystemNodeInfo) -> Unit,
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
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(items = loadState.dataList, key = { it.id }) {
                ParentNodeCard(
                    nodeInfo = it,
                    expandedIdSet = loadState.operatedIdSet,
                    toggleExpand = toggleExpand,
                    itemClick = itemClick
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
    itemClick: (SystemNodeInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        shape = WanDevelopShapes.medium
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable {
                        toggleExpand(nodeInfo)
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = nodeInfo.name)
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
                            .padding(10.dp)
                            .clickable {
                                itemClick(it)
                            }
                    )
                }
            }
        }
    }
}