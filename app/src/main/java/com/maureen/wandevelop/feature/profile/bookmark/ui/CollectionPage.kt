package com.maureen.wandevelop.feature.profile.bookmark.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.maureen.wandevelop.core.entity.Feed
import com.maureen.wandevelop.feature.profile.bookmark.BookmarkViewModel
import com.maureen.wandevelop.ui.composable.FeedPullToRefreshBox

/**
 * 我的收藏
 * @date 2024/5/16
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CollectionPage(
    onCollectionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookmarkViewModel = viewModel()
) {
    val collectionList = viewModel.collectionFlow.collectAsLazyPagingItems()
    val showBottomSheet = remember { mutableStateOf<Feed?>(null) }

    showBottomSheet.value?.also {
        DelBookmarkBottomSheet(
            bookmarkId = it.id,
            onCancel = {
                showBottomSheet.value = null
            },
            onConfirm = { id ->
                showBottomSheet.value = null
                viewModel.cancelCollect(id)
            }
        )
    }
    FeedPullToRefreshBox(
        pagingItems = collectionList,
        modifier = modifier,
        showCollectButton = false,
        showMoreButton = false,
        onItemClick = { onCollectionClick(it.url) },
        onItemLongClick = { showBottomSheet.value = it }
    )
}