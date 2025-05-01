package com.maureen.wandevelop.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maureen.wandevelop.R
import com.maureen.wandevelop.core.entity.WanDevNavDestination
import com.maureen.wandevelop.entity.ProfileInfo
import com.maureen.wandevelop.ui.composable.WanDevTpoAppBar
import com.maureen.wandevelop.ui.theme.WanDevelopTheme
import com.maureen.wandevelop.ui.tooling.UiModePreviews

/**
 * @author lianml
 * @date 2025/3/13
 */
@Composable
internal fun ProfileScreen(
    otherEntranceList: List<WanDevNavDestination<ProfileRoute>>,
    notificationClick: (Int) -> Unit,
    signInClick: () -> Unit,
    entranceItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }
    val profileInfo by viewModel.profileInfoState.collectAsStateWithLifecycle()

    ProfileScreen(
        otherEntranceList = otherEntranceList,
        notificationClick = notificationClick,
        signInClick = signInClick,
        entranceItemClick = entranceItemClick,
        unreadMsgCount = profileInfo.unreadMsgCount,
        profileInfo = profileInfo,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreen(
    otherEntranceList: List<WanDevNavDestination<ProfileRoute>>,
    notificationClick: (Int) -> Unit,
    signInClick: () -> Unit,
    entranceItemClick: (Int) -> Unit,
    unreadMsgCount: Int,
    profileInfo: ProfileInfo,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            WanDevTpoAppBar(
                title = {},
                modifier = Modifier.fillMaxWidth(),
                actions = {
                    BadgedBox(
                        badge = {
                            if (unreadMsgCount > 0) {
                                Badge(containerColor = Color.Red) {
                                    Text(text = unreadMsgCount.toString())
                                }
                            }
                        },
                        content = {
                            IconButton(
                                onClick = { notificationClick(unreadMsgCount) },
                                content = {
                                    Icon(
                                        imageVector = Icons.Outlined.Notifications,
                                        contentDescription = "",
                                    )
                                }
                            )
                        }
                    )
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier.padding(
                    top = padding.calculateTopPadding(),
                    start = 14.dp,
                    end = 14.dp
                )
            ) {
                UserBriefInfoColumn(
                    profileInfo = profileInfo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (profileInfo.name.isBlank()) {
                                signInClick()
                            }
                        }
                )

                UserDataCard(
                    dataList = mapOf(
                        R.string.prompt_level to profileInfo.level,
                        R.string.prompt_coin to profileInfo.coin,
                        R.string.prompt_rank to profileInfo.rank
                    ),
                    onItemClick = entranceItemClick,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
                EntranceColumn(
                    otherEntranceList = otherEntranceList,
                    entranceItemClick = entranceItemClick,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }
        }
    )
}

@Composable
private fun UserBriefInfoColumn(
    profileInfo: ProfileInfo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.AccountCircle,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(72.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        if (profileInfo.name.isBlank()) {
            Text(
                text = stringResource(R.string.nav_sign_in_or_up),
                modifier = Modifier,
                style = MaterialTheme.typography.titleMedium
            )
        } else {
            Text(
                text = profileInfo.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = profileInfo.email,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 用户数据卡片
 */
@Composable
private fun UserDataCard(
    dataList: Map<Int, String>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceBright,
                shape = MaterialTheme.shapes.large
            ),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        dataList.onEachIndexed { index, value ->
            Column(
                modifier = Modifier
                    .clickable { onItemClick(value.key) }
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(value.key))
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = value.value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun EntranceColumn(
    otherEntranceList: List<WanDevNavDestination<ProfileRoute>>,
    entranceItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val columnItemModifier = Modifier.fillMaxWidth()

    Column(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.surfaceBright,
            shape = MaterialTheme.shapes.large
        )
    ) {
        otherEntranceList.forEach {
            Row(
                modifier = columnItemModifier
                    .clickable { entranceItemClick(it.labelId) }
                    .padding(horizontal = 12.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(it.labelId),
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null
                )
            }
        }
    }
}

@UiModePreviews
@Composable
private fun PreviewUserBriefColumn() {
    val profileInfo = ProfileInfo(name = "test", email = "xxxx@xx.com")
    WanDevelopTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            UserBriefInfoColumn(
                profileInfo = profileInfo
            )
        }
    }
}

@UiModePreviews
@Composable
private fun PreviewUserDataCard() {
    val profileInfo = ProfileInfo(level = "Lv.191", coin = "111", rank = "No.146")
    WanDevelopTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            UserDataCard(
                dataList = mapOf(
                    R.string.prompt_level to profileInfo.level,
                    R.string.prompt_coin to profileInfo.coin,
                    R.string.prompt_rank to profileInfo.rank
                ),
                onItemClick = {},
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.large
                    )
            )
        }
    }
}


@UiModePreviews
@Composable
private fun PreviewEntrancesCard() {

    WanDevelopTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            EntranceColumn(
                otherEntranceList = emptyList(),
                entranceItemClick = { },
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }
    }
}