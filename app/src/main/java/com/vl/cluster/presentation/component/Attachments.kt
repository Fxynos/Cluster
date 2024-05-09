package com.vl.cluster.presentation.component

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageAttachment(images: List<Bitmap>, isCarousel: Boolean = images.size > 10) {
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
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            bitmap = images[it].asImageBitmap(),
                            contentDescription = null
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            DotTab(pagerState)
        }
    } else when (images.size) {
        1 -> Card(shape = RoundedCornerShape(cornerRadius)) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                bitmap = images.first().asImageBitmap(),
                contentDescription = null
            )
        }

        2 -> Row(modifier = Modifier.aspectRatio(1.5f)) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(cornerRadius)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    bitmap = images.first().asImageBitmap(),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(gap))
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(cornerRadius)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    bitmap = images[1].asImageBitmap(),
                    contentDescription = null
                )
            }
        }

        3 -> Row(modifier = Modifier.aspectRatio(1f)) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(cornerRadius)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    bitmap = images.first().asImageBitmap(),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(gap))
            Column(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[1].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.height(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[2].asImageBitmap(),
                        contentDescription = null
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
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images.first().asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.height(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[2].asImageBitmap(),
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.width(gap))
            Column(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[1].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.height(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[3].asImageBitmap(),
                        contentDescription = null
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
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images.first().asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[1].asImageBitmap(),
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(gap))
            Row(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[2].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[3].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[4].asImageBitmap(),
                        contentDescription = null
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
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images.first().asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[1].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[2].asImageBitmap(),
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(gap))
            Row(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[3].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[4].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[5].asImageBitmap(),
                        contentDescription = null
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
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images.first().asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[1].asImageBitmap(),
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(gap))
            Row(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[2].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[3].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[4].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[5].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[6].asImageBitmap(),
                        contentDescription = null
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
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images.first().asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[1].asImageBitmap(),
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(gap))
            Row(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[2].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[3].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[4].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[5].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[6].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[7].asImageBitmap(),
                        contentDescription = null
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
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images.first().asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[1].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[2].asImageBitmap(),
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(gap))
            Row(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[3].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[4].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[5].asImageBitmap(),
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(gap))
            Row(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[6].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[7].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[8].asImageBitmap(),
                        contentDescription = null
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
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images.first().asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[1].asImageBitmap(),
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(gap))
            Row(modifier = Modifier.weight(1f)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[2].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[3].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[4].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[5].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[6].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[7].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[8].asImageBitmap(),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(gap))
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(cornerRadius)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        bitmap = images[9].asImageBitmap(),
                        contentDescription = null
                    )
                }
            }
        }

        else -> throw IllegalArgumentException("Use carousel for huge amount of images")
    }
}