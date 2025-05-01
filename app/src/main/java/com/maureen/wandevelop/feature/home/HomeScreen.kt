package com.maureen.wandevelop.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.maureen.wandevelop.ui.composable.FeedPullToRefreshBox
import com.maureen.wandevelop.ui.composable.WanDevTpoAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    onFeedClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    Column(modifier = modifier.fillMaxSize()) {
        val bannerListState by viewModel.bannerListState.collectAsStateWithLifecycle()
        val feedPagingItems = viewModel.feedListFlow.collectAsLazyPagingItems()
        val collectedIds by viewModel.collectedIsSetFlow.collectAsStateWithLifecycle()
        WanDevTpoAppBar(
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

        if (bannerListState.first) {
            HorizontalPager(
                state = rememberPagerState { bannerListState.second.dataList.size },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.26F),
                pageContent = { index ->
                    val banner = bannerListState.second.dataList[index]
                    Box(modifier = Modifier.fillMaxWidth()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(banner.imagePath)
                                .build(),
                            modifier = Modifier
                                .fillMaxHeight()
                                .clip(MaterialTheme.shapes.medium),
                            contentDescription = banner.title,
                            contentScale = ContentScale.Crop,
                        )
                        /*Text(
                            text = banner.title,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.onSurface).alpha(0.8F)
                                .padding(vertical = 4.dp)
                                .align(alignment = Alignment.BottomCenter)
                        )*/
                    }
                }
            )
        }
        FeedPullToRefreshBox(
            pagingItems = feedPagingItems,
            collectedFeedIdSet = collectedIds,
            onItemClick = { onFeedClick(it.url) },
            toggleCollect = { feed, collect -> viewModel.toggleFeedCollect(feed, collect) }
        )
    }
}