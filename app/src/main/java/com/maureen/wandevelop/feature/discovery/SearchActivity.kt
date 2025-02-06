@file:OptIn(ExperimentalLayoutApi::class)

package com.maureen.wandevelop.feature.discovery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.maureen.wandevelop.R
import com.maureen.wandevelop.common.ArticleCard
import com.maureen.wandevelop.common.SearchView
import com.maureen.wandevelop.common.theme.WanDevelopTheme
import com.maureen.wandevelop.common.theme.WanDevelopTypography
import com.maureen.wandevelop.entity.Article
import com.maureen.wandevelop.entity.Hotkey

class SearchActivity : ComponentActivity() {
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WanDevelopTheme {
                SearchScreen(onBackIconClick = { finish() }, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onBackIconClick: () -> Unit = {},
    viewModel: SearchViewModel
) {
    val keywordState by viewModel.searchKeywordState.collectAsStateWithLifecycle()
    val hotkeyState by viewModel.hotkeyUiState.collectAsStateWithLifecycle()
    val searchHistoryState by viewModel.historyKeyUiState.collectAsStateWithLifecycle()
    val searchResultList = viewModel.searchResultFlow.collectAsLazyPagingItems()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        SearchToolbar(
            onBackIconClick = onBackIconClick,
            query = keywordState.first,
            onValueChange = { viewModel.onSearchKeywordChanged(it) },
            onSearch = { viewModel.onSearchKeywordChanged(it, true) }
        )
        if (keywordState.second) {
            SearchResultContent(searchResultList)
        } else {
            if (hotkeyState.first) {
                HotKeyFlowRow(
                    hotkeyList = hotkeyState.second,
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
fun SearchToolbar(
    query: String,
    modifier: Modifier = Modifier,
    onBackIconClick: () -> Unit,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton(onClick = { onBackIconClick.invoke() }) {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        SearchView(
            modifier = modifier
                .absolutePadding(top = 6.dp, bottom = 6.dp, right = 8.dp),
            placeholderText = "搜索关键词以空格形式隔开",
            query = query,
            onValueChange = onValueChange,
            onSearch = onSearch
        )
    }
}

@Composable
fun SearchResultContent(pagingItems: LazyPagingItems<Article>, modifier: Modifier = Modifier) {
    if (pagingItems.loadState.refresh is LoadState.Loading) {

    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            count = pagingItems.itemCount,
            key = pagingItems.itemKey { it.id }
        ) { index ->
            val article = pagingItems[index] ?: return@items
            ArticleCard(
                article = article,
                onCollectClick = {},
                onMoreClick = {},
                onItemClick = {}
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchHistoryFlowRow(
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
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("历史搜索", color = MaterialTheme.colorScheme.onSurface)
            if (isDeleteModeOn) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "删除全部",
                        modifier = Modifier.clickable {
                            onDeleteClick.invoke(historyList)
                        },
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = WanDevelopTypography.labelLarge
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        "完成",
                        modifier = Modifier.clickable {
                            onDeleteModeChange.invoke(false)
                        },
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = WanDevelopTypography.labelLarge
                    )
                }
            } else if (historyList.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onDeleteModeChange.invoke(true)
                    },
                    modifier = Modifier.size(26.dp),
                    content = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                            "删除",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    })
            }
        }
        Spacer(modifier = Modifier.size(10.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            historyList.forEach {
                Row(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            shape = RoundedCornerShape(corner = CornerSize(18.dp))
                        )
                        .padding(10.dp)
                        .clickable {
                            if (isDeleteModeOn) {
                                onDeleteClick.invoke(listOf(it))
                            } else {
                                onHistoryClick.invoke(it)
                            }
                        },
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = WanDevelopTypography.labelLarge
                    )
                    if (isDeleteModeOn) {
                        Icon(
                            Icons.Default.Clear,
                            null,
                            modifier = Modifier
                                .size(14.dp)
                                .padding(2.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HotKeyFlowRow(
    hotkeyList: List<Hotkey>,
    modifier: Modifier = Modifier,
    onHotkeyClick: (Hotkey) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Text("热门搜索", color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.size(10.dp))
        FlowRow(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            hotkeyList.forEach {
                Text(
                    text = it.name,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            shape = RoundedCornerShape(corner = CornerSize(18.dp))
                        )
                        .clickable {
                            onHotkeyClick.invoke(it)
                        }
                        .padding(10.dp),
                    color = MaterialTheme.colorScheme.inverseSurface,
                    style = WanDevelopTypography.labelLarge,
                )
            }
        }
    }
}

@Preview
@Composable
fun SearchToolbarPreview() {
    WanDevelopTheme(darkThemeOn = false) {
        Surface(modifier = Modifier.background(color = MaterialTheme.colorScheme.surface)) {
            SearchToolbar(
                onBackIconClick = {},
                query = "",
                onValueChange = {},
                onSearch = {}
            )
        }
    }
}

@Preview
@Composable
fun SearchHistoryFlowRowPreview() {
    val historyList = mutableListOf<String>()
    repeat(10) {
        historyList.add("compose${it}")
    }
    val deleteMode = remember { mutableStateOf(true) }
    WanDevelopTheme(darkThemeOn = false) {
        Surface(modifier = Modifier.background(color = MaterialTheme.colorScheme.surface)) {
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

@Preview
@Composable
fun HotkeyFlowRowPreview() {
    val hotkeyList = mutableListOf<Hotkey>()
    repeat(10) {
        hotkeyList.add(Hotkey(it, "", "compose $it", 0, 1))
    }
    WanDevelopTheme(darkThemeOn = false) {
        Surface(modifier = Modifier.background(color = MaterialTheme.colorScheme.surface)) {
            HotKeyFlowRow(hotkeyList = hotkeyList,
                onHotkeyClick = { })
        }
    }
}