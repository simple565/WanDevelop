package com.maureen.wandevelop.feature.profile.bookmark.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.maureen.wandevelop.R
import com.maureen.wandevelop.feature.profile.bookmark.BookmarkViewModel
import com.maureen.wandevelop.ui.composable.WanDevTpoAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BookmarkScreen(
    onBackClick: () -> Unit,
    onBookmarkClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            WanDevTpoAppBar(
                title = stringResource(R.string.nav_my_bookmark),
                onNavigationIconClick = onBackClick
            )
        },
        content = { paddingValue ->
            val pagerState =
                rememberPagerState(pageCount = { BookmarkViewModel.bookmarkNames.size })
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValue),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BookmarkTypeRow(
                    selectedIndex = pagerState.currentPage,
                    bookmarkNameResIdList = BookmarkViewModel.bookmarkNames,
                    onBookmarkTypeSelect = {
                        pagerState.requestScrollToPage(page = it)
                    },
                    modifier = Modifier
                )
                HorizontalPager(state = pagerState) {
                    if (it == 0) {
                        CollectionPage(
                            onCollectionClick = onBookmarkClick,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        ReadLaterPage(
                            onCollectionClick = {},
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun BookmarkTypeRow(
    selectedIndex: Int,
    bookmarkNameResIdList: List<Int>,
    onBookmarkTypeSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        indicator = {},
        tabs = {
            bookmarkNameResIdList.forEachIndexed { index, nameResId ->
                Tab(
                    onClick = { onBookmarkTypeSelect(index) },
                    selected = index == selectedIndex,
                    text = {
                        Text(
                            text = stringResource(nameResId),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    },
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}