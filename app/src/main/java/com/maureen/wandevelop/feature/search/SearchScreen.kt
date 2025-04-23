package com.maureen.wandevelop.feature.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.maureen.wandevelop.R
import com.maureen.wandevelop.common.composable.FeedPullToRefreshBox
import com.maureen.wandevelop.common.composable.SearchView
import com.maureen.wandevelop.common.theme.WanDevelopTheme
import com.maureen.wandevelop.common.theme.WanDevelopTypography
import com.maureen.wandevelop.common.tooling.UiModePreviews
import com.maureen.wandevelop.entity.HotkeyInfo

@Composable
internal fun SearchScreen(
    onBackClick: () -> Unit,
    onFeedClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel()
) {
    val keywordState by viewModel.searchKeywordState.collectAsStateWithLifecycle()
    val hotkeyState by viewModel.hotkeyInfoUiState.collectAsStateWithLifecycle()
    val searchHistoryState by viewModel.historyKeyUiState.collectAsStateWithLifecycle()
    val searchResultList = viewModel.searchResultFlow.collectAsLazyPagingItems()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SearchToolbar(
            onBackIconClick = onBackClick,
            modifier = modifier
                .fillMaxWidth()
                .systemBarsPadding(),
            query = keywordState.first,
            onValueChange = { viewModel.onSearchKeywordChanged(it) },
            onSearch = { viewModel.onSearchKeywordChanged(it, true) }
        )
        if (keywordState.second) {
            FeedPullToRefreshBox(
                pagingItems = searchResultList,
                showMoreButton = false,
                showCollectButton = false,
                onItemClick = { onFeedClick(it.url) }
            )
        } else {
            if (hotkeyState.first) {
                HotKeyFlowRow(
                    hotkeyInfoList = hotkeyState.second,
                    onHotkeyClick = { viewModel.onSearchKeywordChanged(it.name, true) }
                )
            }
            if (searchHistoryState.second.isNotEmpty()) {
                SearchHistoryFlowRow(
                    historyList = searchHistoryState.second,
                    isDeleteModeOn = searchHistoryState.first,
                    onDeleteModeChange = { viewModel.deleteSearchHistory(if (it) emptyList() else null) },
                    onDeleteClick = { viewModel.deleteSearchHistory(it) },
                    onHistoryClick = { viewModel.onSearchKeywordChanged(it, true) }
                )
            }
        }
    }
}

@Composable
internal fun SearchToolbar(
    query: String,
    modifier: Modifier = Modifier,
    onBackIconClick: () -> Unit,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(vertical = 6.dp)
    ) {
        IconButton(
            onClick = { onBackIconClick.invoke() },
            modifier = Modifier.size(36.dp),
            content = {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        )
        SearchView(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.75F),
            placeholderText = stringResource(R.string.prompt_search_hint),
            query = query,
            onValueChange = onValueChange,
            onSearch = onSearch
        )
        Text(
            text = stringResource(R.string.search),
            style = WanDevelopTypography.labelLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .wrapContentSize(align = Alignment.Center)
                .padding(10.dp)
                .clickable { onSearch(query) }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun SearchHistoryFlowRow(
    historyList: List<String>,
    modifier: Modifier = Modifier,
    isDeleteModeOn: Boolean = false,
    onDeleteModeChange: (Boolean) -> Unit = {},
    onDeleteClick: (List<String>) -> Unit = {},
    onHistoryClick: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.prompt_search_history),
                style = MaterialTheme.typography.titleSmall
            )
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isDeleteModeOn) {
                    Text(
                        text = stringResource(R.string.delete_all),
                        modifier = Modifier.clickable {
                            onDeleteClick.invoke(historyList)
                        },
                        style = WanDevelopTypography.labelLarge
                    )
                    Text(
                        text = stringResource(R.string.done),
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .clickable {
                                onDeleteModeChange.invoke(false)
                            },
                        style = WanDevelopTypography.labelLarge
                    )
                } else if (historyList.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onDeleteModeChange.invoke(true)
                        },
                        modifier = Modifier.size(28.dp),
                        content = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                                contentDescription = stringResource(R.string.delete),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )
                }
            }
        }
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            historyList.forEach {
                Row(
                    modifier = Modifier
                        .clickable {
                            if (isDeleteModeOn) {
                                onDeleteClick.invoke(listOf(it))
                            } else {
                                onHistoryClick.invoke(it)
                            }
                        }
                        .background(
                            color = MaterialTheme.colorScheme.surfaceBright,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = it,
                        style = WanDevelopTypography.labelLarge
                    )
                    if (isDeleteModeOn) {
                        Icon(
                            Icons.Default.Clear,
                            null,
                            modifier = Modifier
                                .padding(start = 2.dp)
                                .size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun HotKeyFlowRow(
    hotkeyInfoList: List<HotkeyInfo>,
    modifier: Modifier = Modifier,
    onHotkeyClick: (HotkeyInfo) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = stringResource(R.string.prompt_hotkey),
            style = MaterialTheme.typography.titleSmall
        )
        FlowRow(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            hotkeyInfoList.forEach {
                Text(
                    text = it.name,
                    modifier = Modifier
                        .clickable {
                            onHotkeyClick.invoke(it)
                        }
                        .background(
                            color = MaterialTheme.colorScheme.surfaceBright,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(10.dp),
                    style = WanDevelopTypography.labelLarge,
                )
            }
        }
    }
}

@UiModePreviews
@Composable
fun SearchToolbarPreview() {
    WanDevelopTheme {
        Surface(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
            SearchToolbar(
                onBackIconClick = {},
                query = "",
                onValueChange = {},
                onSearch = {}
            )
        }
    }
}

@UiModePreviews
@Composable
fun SearchHistoryFlowRowPreview() {
    val historyList = mutableListOf<String>()
    repeat(10) {
        historyList.add("compose${it}")
    }
    val deleteMode = remember { mutableStateOf(true) }
    WanDevelopTheme {
        Surface(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
            SearchHistoryFlowRow(
                historyList = historyList,
                isDeleteModeOn = deleteMode.value,
                onDeleteModeChange = {
                    deleteMode.value = it
                },
                onDeleteClick = {

                },
                onHistoryClick = {

                })
        }
    }
}

@UiModePreviews
@Composable
fun HotkeyFlowRowPreview() {
    val hotkeyInfoList = mutableListOf<HotkeyInfo>()
    repeat(10) {
        hotkeyInfoList.add(HotkeyInfo(it, "", "compose $it", 0, 1))
    }
    WanDevelopTheme {
        Surface(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
            HotKeyFlowRow(
                hotkeyInfoList = hotkeyInfoList,
                onHotkeyClick = { }
            )
        }
    }
}