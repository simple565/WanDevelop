package com.maureen.wandevelop.feature.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.maureen.wandevelop.common.composable.FeedPagingColumn
import com.maureen.wandevelop.common.entity.Feed
import com.maureen.wandevelop.feature.discovery.composable.CourseListPage
import com.maureen.wandevelop.feature.discovery.composable.RouteListPage
import kotlinx.coroutines.launch

class DiscoveryFragment : Fragment() {
    companion object {
        private const val TAG = "DiscoveryFragment"
    }

    private val viewModel: DiscoveryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            this.setContent {
                DiscoveryScreen(viewModel = viewModel)
            }
        }
    }

    @Composable
    fun DiscoveryScreen(viewModel: DiscoveryViewModel, modifier: Modifier = Modifier) {
        val pagerState = rememberPagerState(pageCount = { viewModel.pageList.size })

        Column(modifier = modifier.fillMaxSize()) {
            val coroutineScope = rememberCoroutineScope()
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth()
            ) {
                viewModel.pageList.forEachIndexed { index, i ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(text = stringResource(i.titleRes)) },
                        unselectedContentColor = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            HorizontalPager(state = pagerState, modifier = Modifier) { index ->
                val page = viewModel.pageList.getOrNull(index) ?: return@HorizontalPager
                when (page) {
                    DiscoveryPage.COURSE -> {
                        val loadState by viewModel.loadCourseListState.collectAsStateWithLifecycle()
                        CourseListPage(loadState)
                    }

                    DiscoveryPage.ROUTE -> {
                        val loadState by viewModel.loadRouteListState.collectAsStateWithLifecycle()
                        RouteListPage(
                            loadState = loadState,
                            toggleExpand = viewModel::toggleRouteExpandState,
                            itemClick = {}
                        )
                    }

                    else -> {
                        PagingPage(viewModel.getPagingFlow(page).collectAsLazyPagingItems())
                    }
                }
            }
        }
    }

    @Composable
    fun PagingPage(
        pagingItems: LazyPagingItems<Feed>,
        modifier: Modifier = Modifier
    ) {
        val scrollState = rememberLazyListState()
        FeedPagingColumn(
            pagingItems = pagingItems,
            modifier = modifier.scrollable(
                state = scrollState,
                orientation = Orientation.Vertical
            ),
            showCollectButton = true,
            showMoreButton = true,
        )
    }
}