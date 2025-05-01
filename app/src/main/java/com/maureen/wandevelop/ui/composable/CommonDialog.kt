package com.maureen.wandevelop.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Stable
fun Modifier.defaultDialogWidth() = this.then(Modifier.width(180.dp))

@Stable
fun Modifier.defaultDialogHeight() = this.then(Modifier.heightIn(180.dp))

@Stable
fun Modifier.defaultDialogSize() = this.then(Modifier.defaultDialogWidth().defaultDialogHeight())


/**
 * 进度对话框
 */
@Composable
fun ProgressDialog(
    message: String,
    modifier: Modifier = Modifier,
    onDismissRequest: ()-> Unit = {},
    progress: Float? = null
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = {
            Surface(
                modifier = modifier.then(Modifier.defaultDialogSize()),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.size(24.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    )
}


