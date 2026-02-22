package com.maureen.wandevelop.feature.home

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.maureen.wandevelop.ui.composable.BannerContainer
import com.maureen.wandevelop.ui.composable.FeedPullToRefreshBox
import com.maureen.wandevelop.ui.composable.WanDevTopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    onFeedClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            WanDevTopAppBar(
                title = {},
                actions = {
                    IconButton(
                        onClick = onSearchClick,
                        content = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = ""
                            )
                        }
                    )
                }
            )
        }
    ) { innerPadding ->
        val bannerListState by viewModel.bannerListState.collectAsStateWithLifecycle()
        val feedPagingItems = viewModel.feedListFlow.collectAsLazyPagingItems()
        val collectedIds by viewModel.collectedIsSetFlow.collectAsStateWithLifecycle()

        FeedPullToRefreshBox(
            pagingItems = feedPagingItems,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            header = {
                if (bannerListState.first) {
                    BannerContainer(
                        bannerInfos = bannerListState.second.dataList,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.26F)
                    )
                }
            },
            collectedFeedIdSet = collectedIds,
            onItemClick = { onFeedClick(it.url) },
            toggleCollect = { feed, collect -> viewModel.toggleFeedCollect(feed, collect) },
            onMoreClick = {

            }
        )
    }
}