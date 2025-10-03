package com.maureen.wandevelop.feature.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maureen.wandevelop.R
import com.maureen.wandevelop.entity.SettingItem
import com.maureen.wandevelop.entity.SettingType
import com.maureen.wandevelop.ui.composable.ProgressDialog
import com.maureen.wandevelop.ui.composable.WanDevTopAppBar
import com.maureen.wandevelop.ui.theme.WanDevelopTypography


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel()
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            WanDevTopAppBar(
                title = stringResource(R.string.nav_settings),
                onNavigationIconClick = onBackClick
            )
        },
        content = { paddingValues ->
            val settingItems by viewModel.settingItemsFlow.collectAsStateWithLifecycle()
            val operationState by viewModel.operationState.collectAsStateWithLifecycle()
            if (operationState.isOperating) {
                ProgressDialog(operationState.operatingMsg)
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 10.dp,
                        end = 10.dp,
                        top = paddingValues.calculateTopPadding(),
                        bottom = 0.dp
                    ),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val itemContentPaddingValues = PaddingValues(10.dp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceBright,
                            shape = MaterialTheme.shapes.large
                        )
                ) {
                    settingItems.forEach {
                        SettingItem(
                            item = it,
                            onAction = viewModel::settingAction,
                            onCheckChange = viewModel::updateSetting,
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = itemContentPaddingValues
                        )
                    }
                }

                TextButton(
                    onClick = { viewModel.signOut() },
                    content = {
                        Text(
                            text = stringResource(R.string.nav_sign_out),
                            style = WanDevelopTypography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.textButtonColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
            }
        }
    )
}

@Composable
private fun SettingItem(
    item: SettingItem,
    onCheckChange: (Int, Boolean) -> Unit,
    onAction: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues
) {
    Row(
        modifier = modifier
            .clickable {
                if (item.type == SettingType.ACTION) {
                    onAction(item.type)
                }
            }
            .padding(contentPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(item.name),
            style = MaterialTheme.typography.titleMedium
        )
        if (item.type == SettingType.ACTION) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_right),
                contentDescription = null,
                modifier = Modifier.minimumInteractiveComponentSize()
            )
        } else {
            Switch(
                checked = item.value.toBoolean(),
                onCheckedChange = { onCheckChange(item.name, it) },
                modifier = Modifier.scale(0.6F)
            )
        }
    }
}

@Preview
@Composable
private fun SettingItemPreview() {
    val item = SettingItem(
        name = R.string.prompt_dark_mode_follow_system,
        value = "false"
    )
    MaterialTheme {
        Column {
            SettingItem(
                item = item,
                onCheckChange = { id, value -> },
                onAction = {},
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 16.dp)
            )
        }
    }
}