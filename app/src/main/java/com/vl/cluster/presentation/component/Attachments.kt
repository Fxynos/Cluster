package com.vl.cluster.presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageAttachment(images: List<String>, isCarousel: Boolean = images.size > 10) {
    val cornerRadius = 4.dp
    val gap = 4.dp

    if (isCarousel) {
        val pagerState = rememberPagerState(pageCount = images::size)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = gap * 2)
            ) {
                Box(modifier = Modifier.padding(gap)) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        shape = RoundedCornerShape(cornerRadius)
                    ) {
                        Picture(
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            image = images[it]
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            DotTab(pagerState)
        }
    } else when (images.size) {
        1 -> Card(shape = RoundedCornerShape(cornerRadius)) {
            Picture(
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                image = images.first()
            )
        }

        2 -> Row(modifier = Modifier.aspectRatio(1.5f)) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(cornerRadius)
            ) {
                Picture(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    image = images.first()
                )
            }
            Spacer(modifier = Modifier.width(gap))
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(cornerRadius)
            ) {
                Picture(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    image = images[1]
                )
            }
        }

        3 -> Row(modifier = Modifier.aspectRatio(1f)) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(cornerRadius)
            ) {
                Picture(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    image = images.first()
                )
            }
            Spacer(modifier = Modifier.width(gap))
            Column(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[1]
                    )
                }
                Spacer(modifier = Modifier.height(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[2]
                    )
                }
            }
        }

        4 -> Row(modifier = Modifier.aspectRatio(1f)) {
            Column(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images.first()
                    )
                }
                Spacer(modifier = Modifier.height(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[2]
                    )
                }
            }
            Spacer(modifier = Modifier.width(gap))
            Column(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[1]
                    )
                }
                Spacer(modifier = Modifier.height(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[3]
                    )
                }
            }
        }

        5 -> Column(modifier = Modifier.aspectRatio(1.5f)) {
            Row(modifier = Modifier.weight(2f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images.first()
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[1]
                    )
                }
            }
            Spacer(modifier = Modifier.height(gap))
            Row(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[2]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[3]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[4]
                    )
                }
            }
        }

        6 -> Column(modifier = Modifier.aspectRatio(1.5f)) {
            Row(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images.first()
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[1]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[2]
                    )
                }
            }
            Spacer(modifier = Modifier.height(gap))
            Row(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[3]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[4]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[5]
                    )
                }
            }
        }

        7 -> Column(modifier = Modifier.aspectRatio(1.5f)) {
            Row(modifier = Modifier.weight(3f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images.first()
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[1]
                    )
                }
            }
            Spacer(modifier = Modifier.height(gap))
            Row(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[2]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[3]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[4]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[5]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[6]
                    )
                }
            }
        }

        8 -> Column(modifier = Modifier.aspectRatio(1.5f)) {
            Row(modifier = Modifier.weight(3f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images.first()
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[1]
                    )
                }
            }
            Spacer(modifier = Modifier.height(gap))
            Row(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[2]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[3]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[4]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[5]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[6]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[7]
                    )
                }
            }
        }

        9 -> Column(modifier = Modifier.aspectRatio(1f)) {
            Row(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images.first()
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[1]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[2]
                    )
                }
            }
            Spacer(modifier = Modifier.height(gap))
            Row(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[3]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[4]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[5]
                    )
                }
            }
            Spacer(modifier = Modifier.height(gap))
            Row(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[6]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[7]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[8]
                    )
                }
            }
        }

        10 -> Column(modifier = Modifier.aspectRatio(2f)) {
            Row(modifier = Modifier.weight(3f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images.first()
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[1]
                    )
                }
            }
            Spacer(modifier = Modifier.height(gap))
            Row(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[2]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[3]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[4]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[5]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[6]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[7]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[8]
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Picture(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        image = images[9]
                    )
                }
            }
        }

        else -> throw IllegalArgumentException("Use carousel for huge amount of images")
    }
}