package com.maureen.wandevelop.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.maureen.wandevelop.network.entity.BannerInfo
import com.maureen.wandevelop.ui.theme.WanDevelopTheme
import com.maureen.wandevelop.ui.tooling.UiModePreviews

/**
 * @author lianml
 * @date 2025/2/7
 */
@Composable
fun BannerContainer(
    bannerInfos: List<BannerInfo>,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState { bannerInfos.size }
) {
    if (bannerInfos.isEmpty()) {
        Box(
            modifier = modifier.background(
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.medium
            )
        )
    } else {
        Box(modifier = modifier) {
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(bannerInfos[page].imagePath)
                        .crossfade(true)
                        .scale(Scale.FILL)
                        .build(),
                    contentDescription = bannerInfos[page].title,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.FillWidth
                )
            }
            Text(
                text = bannerInfos[pagerState.settledPage].title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(0.4F),
                        shape = MaterialTheme.shapes.medium.copy(
                            topStart = CornerSize(0.dp),
                            topEnd = CornerSize(0.dp)
                        )
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .align(alignment = Alignment.BottomCenter)
            )
        }
    }
}

@UiModePreviews
@Composable
fun BannerContainerPreview() {
    val bannerInfo = BannerInfo(
        desc = "我们支持订阅啦",
        id = 10,
        imagePath = "https://www.wanandroid.com/blogimgs/42da12d8-de56-4439-b40c-eab66c227a4b.png",
        order = 0,
        title = "我们支持订阅啦",
        type = 0,
        isVisible = 1,
        url = "https://www.wanandroid.com/blog/show/3352"
    )
    val list = mutableListOf<BannerInfo>()
    repeat(4) {
        list.add(bannerInfo)
    }
    WanDevelopTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            BannerContainer(bannerInfos = list, modifier = Modifier.height(250.dp))
        }
    }
}