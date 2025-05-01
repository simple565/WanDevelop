package com.maureen.wandevelop.feature.profile.bookmark.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.maureen.wandevelop.db.ReadRecord
import com.maureen.wandevelop.feature.profile.bookmark.BookmarkViewModel
import com.maureen.wandevelop.ui.composable.FeedPullToRefreshBox

/**
 * 稍候阅读页面
 * @date 2024/5/16
 */
@Composable
internal fun ReadLaterPage(
    onCollectionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookmarkViewModel = viewModel()
) {
    val dataFlow = viewModel.getReadRecordFlow(ReadRecord.TYPE_READ_LATER).collectAsLazyPagingItems()

    FeedPullToRefreshBox(
        pagingItems = dataFlow,
        modifier = modifier,
        showCollectButton = false,
        showMoreButton = false,
        onItemClick = { onCollectionClick(it.url) }
    )

}