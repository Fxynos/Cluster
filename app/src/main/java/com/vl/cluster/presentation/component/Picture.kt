package com.vl.cluster.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage

@Composable
fun Picture(
    modifier: Modifier = Modifier,
    image: String,
    contentScale: ContentScale = ContentScale.Fit
) {
    AsyncImage(
        modifier = modifier,
        model = image,
        contentScale = contentScale,
        contentDescription = null
    )
}

@Composable
fun Picture(
    modifier: Modifier = Modifier,
    @DrawableRes image: Int,
    contentScale: ContentScale = ContentScale.Fit
) {
    Image(
        modifier = modifier,
        painter = painterResource(image),
        contentScale = contentScale,
        contentDescription = null
    )
}