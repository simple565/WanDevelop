package com.maureen.wandevelop.feature.discovery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.maureen.wandevelop.feature.discovery.course.CourseListPage
import com.maureen.wandevelop.ui.composable.FeedPullToRefreshBox
import kotlinx.coroutines.launch

/**
 * 发现页面
 * @date 2025/4/4
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DiscoveryPage(
    onFeedClick: (String) -> Unit,
    onCourseClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DiscoveryViewModel = viewModel()
) {
    Column(
        modifier = modifier.safeContentPadding().fillMaxSize(),
        content = {
            val pagerState = rememberPagerState(pageCount = { viewModel.pageList.size })
            val coroutineScope = rememberCoroutineScope()
            SecondaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.background
            ) {
                viewModel.pageList.forEachIndexed { index, i ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(text = stringResource(i.titleRes)) },
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            HorizontalPager(state = pagerState) { index ->
                val page = viewModel.pageList.getOrNull(index) ?: return@HorizontalPager
                if (page == DiscoveryPage.COURSE) {
                    val loadState by viewModel.loadCourseListState.collectAsStateWithLifecycle()
                    CourseListPage(
                        loadState = loadState,
                        onCourseClick = onCourseClick,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    val pagingState = viewModel.getPagingFlow(page).collectAsLazyPagingItems()
                    val collectedIds by viewModel.collectedIsSetFlow.collectAsStateWithLifecycle()
                    FeedPullToRefreshBox(
                        pagingItems = pagingState,
                        modifier = Modifier.fillMaxSize(),
                        collectedFeedIdSet = collectedIds,
                        onItemClick = { onFeedClick(it.url) },
                        toggleCollect = { feed, collect ->
                            viewModel.toggleFeedCollect(feed, collect)
                        },
                        onMoreClick = {}
                    )
                }
            }
        }
    )
}