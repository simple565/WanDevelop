package com.maureen.wandevelop.feature.profile.bookmark.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.maureen.wandevelop.feature.profile.bookmark.BookmarkViewModel
import com.maureen.wandevelop.ui.composable.FeedPullToRefreshBox

/**
 * 我的收藏
 * @author lianml
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
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false, )

    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(),
            dragHandle = null,
            sheetState = sheetState,
            onDismissRequest = { showBottomSheet = false },
        ) {
            Text(
                text = "Swipe up to open sheet. Swipe down to dismiss.",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
    FeedPullToRefreshBox(
        pagingItems = collectionList,
        modifier = modifier,
        showCollectButton = false,
        showMoreButton = false,
        onItemClick = { onCollectionClick(it.url) },
        onItemLongClick = { showBottomSheet = true }
    )
}