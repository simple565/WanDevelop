package com.maureen.wandevelop.feature.profile.bookmark.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.maureen.wandevelop.R

/**
 * 删除收藏确认底部对话框
 * @date 2025/5/2
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DelBookmarkBottomSheet(
    bookmarkId: Long,
    onCancel: () -> Unit,
    onConfirm: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        modifier = modifier,
        dragHandle = null,
        onDismissRequest = onCancel,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().background(color = MaterialTheme.colorScheme.surfaceBright),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.prompt_delete_article_or_not),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            TextButton(
                onClick = { onConfirm(bookmarkId) },
                modifier = Modifier.fillMaxWidth().background(color = MaterialTheme.colorScheme.surfaceBright),
                shape = RoundedCornerShape(0.dp),
                content = {
                    Text(
                        text = stringResource(R.string.delete),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            )
            TextButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth().background(color = MaterialTheme.colorScheme.surfaceBright),
                shape = RoundedCornerShape(0.dp),
                content = {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
        }

    }
}