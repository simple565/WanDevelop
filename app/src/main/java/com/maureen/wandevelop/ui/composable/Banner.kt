package com.maureen.wandevelop.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.maureen.wandevelop.entity.BannerInfo
import com.maureen.wandevelop.ui.theme.WanDevelopTheme

/**
 * @author lianml
 * @date 2025/2/7
 */
@Composable
fun BannerContainer(pagerState: PagerState, bannerInfos: List<BannerInfo>, modifier: Modifier = Modifier) {

    Box(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(bannerInfos[page].imagePath)
                    .scale(Scale.FILL)
                    .build(),
                contentDescription = bannerInfos[page].title,
                contentScale = ContentScale.FillWidth
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inverseSurface
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(6.dp)
                )
            }
        }
    }

}

@Composable
@Preview
fun BannerContainerPreview() {
    val bannerInfo = BannerInfo(
        desc = "我们支持订阅啦",
        id = 10,
        imagePath = "https://www.wanandroid.com/blogimgs/42da12d8-de56-4439-b40c-eab66c227a4b.png",
        order = 0,
        title = "",
        type = 0,
        isVisible = 1,
        url = "https://www.wanandroid.com/blog/show/3352"
    )
    val list = mutableListOf<BannerInfo>()
    repeat(4) {
        list.add(bannerInfo)
    }
    WanDevelopTheme(darkThemeOn = false) {
        val pagerState = rememberPagerState { list.size }
        BannerContainer(pagerState, list, modifier = Modifier.height(250.dp))
    }
}