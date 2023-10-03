package com.vl.cluster.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vl.cluster.R
import com.vl.cluster.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun WelcomeSliderScreenPreview(
    @PreviewParameter(WelcomeSliderPagePreviewParameterProvider::class)
    pages: List<WelcomeSliderPage>
) {
    AppTheme {
        Surface {
            WelcomeSliderScreen(pages = pages)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeSliderScreen(
    @PreviewParameter(WelcomeSliderPagePreviewParameterProvider::class)
    pages: List<WelcomeSliderPage>,
    onAddAccountClick: () -> Unit = {}
) {
    val coroutineContext = rememberCoroutineScope()
    val pagerState = rememberPagerState { pages.size }
    Box {
        HorizontalPager(pagerState) {
            val page = pages[it]
            Column {
                Image(
                    painter = painterResource(id = page.imageDrawable),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxHeight(0.5f)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.75f)
                        .wrapContentSize(Alignment.Center)
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.primary,
                        text = page.title,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        text = page.text,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(pages.size) { i ->
                    val isCurrent = i == pagerState.currentPage
                    Box(
                        modifier = Modifier
                            .size(if (isCurrent) 20.dp else 12.dp)
                            .padding(4.dp)
                            .background(
                                color = if (isCurrent) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(100)
                            ),
                    ) {}
                }
            }
            val isLast = pagerState.currentPage == pages.lastIndex
            Button(
                onClick = {
                    if (isLast)
                        onAddAccountClick()
                    else
                        coroutineContext.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text =
                    if (isLast)
                        "Добавить аккаунт"
                    else
                        "Продолжить"
                )
            }
        }
    }
}

class WelcomeSliderPagePreviewParameterProvider: PreviewParameterProvider<List<WelcomeSliderPage>> {
    override val values: Sequence<List<WelcomeSliderPage>> = sequenceOf(listOf(
        WelcomeSliderPage(
            "Coke Cola",
            "Lorem ipsum, lorem ipsum.",
            R.drawable.slider_meeting_info
        ),
        WelcomeSliderPage(
            "Coke Cola",
            "Lorem ipsum, lorem ipsum.",
            R.drawable.slider_meeting_info
        ),
        WelcomeSliderPage(
            "Coke Cola",
            "Lorem ipsum, lorem ipsum.",
            R.drawable.slider_meeting_info
        )
    ))
    override val count: Int
        get() = values.count()
}

data class WelcomeSliderPage(
    val title: String,
    val text: String,
    @DrawableRes val imageDrawable: Int
)