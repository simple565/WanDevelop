package com.maureen.wandevelop.feature.profile.bookmark.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.maureen.wandevelop.R
import com.maureen.wandevelop.common.composable.FeedPullToRefreshBox
import com.maureen.wandevelop.common.composable.WanDevTpoAppBar
import com.maureen.wandevelop.db.ReadRecord
import com.maureen.wandevelop.feature.profile.bookmark.BookmarkViewModel

/**
 * 浏览记录
 * @date 2024/5/16
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReadRecordScreen(
    onBackClick: () -> Unit,
    onCollectionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookmarkViewModel = viewModel()
) {
    Column(modifier = modifier.fillMaxSize()) {
        val dataFlow = viewModel.getReadRecordFlow(ReadRecord.TYPE_READ_HISTORY).collectAsLazyPagingItems()

        WanDevTpoAppBar(
            title = stringResource(R.string.nav_read_record),
            onNavigationIconClick = onBackClick
        )

        FeedPullToRefreshBox(
            pagingItems = dataFlow,
            modifier = modifier,
            showCollectButton = false,
            showMoreButton = false,
            onItemClick = { onCollectionClick(it.url) }
        )
    }
}