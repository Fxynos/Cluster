package com.vl.cluster.presentation.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vl.cluster.R
import com.vl.cluster.presentation.component.ImageAttachment
import java.util.stream.Collectors
import java.util.stream.IntStream

@Preview(showSystemUi = true)
@Composable
fun NewsfeedScreen() {
    val loremPostum = listOf(
        Post(
            "Имя Фамилиевич", "28 мая 21:46",
            LoremIpsum(8).values.joinToString(" "),
            null, R.drawable.vk
        ),
        Post(
            "Хорошие мемы", "1 апреля 21:38",
            LoremIpsum(40).values.joinToString(" "),
            null, null
        ),
        Post(
            "Какой-то Пользователь", "2 апреля 4:31",
            LoremIpsum(4).values.joinToString(" "),
            null, R.drawable.vk
        )
    )
    val context = LocalContext.current

    LazyColumn {
        items(12) {
            val post = loremPostum[it % loremPostum.size]
            Spacer(modifier = Modifier.height(4.dp))
            PostComponent(post.title, post.datetime, post.text, post.profile, post.networkIcon,
                IntStream.range(0, it).mapToObj {
                    BitmapFactory.decodeResource(context.resources, R.drawable.img)
                }.collect(Collectors.toList()))
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun PostComponent(
    title: String,
    secondTitle: String,
    text: String,
    profile: Bitmap?,
    @DrawableRes networkIcon: Int?,
    images: List<Bitmap>
) {
    Surface(
        shadowElevation = 2.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        /* Header */
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(all = 12.dp)) {
            Row {
                Box(modifier = Modifier
                    .size(64.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(16.dp)
                    )
                ) {
                    if (profile != null)
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            bitmap = profile.asImageBitmap(),
                            contentDescription = null
                        )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier
                            .size(24.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(8.dp)
                            )
                        ) {
                            if (networkIcon != null)
                                Image(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(networkIcon),
                                    contentDescription = null
                                )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = secondTitle, color = MaterialTheme.colorScheme.outline)
                    }
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(R.drawable.ic_more), contentDescription = null)
                }
            }
            /* Body */
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = text)
            if (images.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                ImageAttachment(images)
            }
        }
    }
}

data class Post(
    val title: String,
    val datetime: String,
    val text: String,
    val profile: Bitmap?,
    @DrawableRes val networkIcon: Int?
)