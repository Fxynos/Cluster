package com.vl.cluster.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DotTab(pagerState: PagerState) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerState.pageCount) { i ->
            val isCurrent = i == pagerState.currentPage
            Box(
                modifier = Modifier
                    .size(if (isCurrent) 20.dp else 12.dp)
                    .padding(4.dp)
                    .background(
                        color = if (isCurrent) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(100)
                    )
            )
        }
    }
}