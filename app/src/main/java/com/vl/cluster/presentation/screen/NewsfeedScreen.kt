package com.vl.cluster.presentation.screen

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.vl.cluster.R
import com.vl.cluster.presentation.component.ImageAttachment
import com.vl.cluster.presentation.component.Picture
import com.vl.cluster.presentation.entity.Post
import com.vl.cluster.presentation.viewmodel.NewsfeedViewModel

@Preview(showSystemUi = true)
@Composable
fun NewsfeedScreenPreview() {
    NewsfeedUi(listOf(
        Post(
            "Имя Фамилиевич", "28 мая 21:46",
            LoremIpsum(8).values.joinToString(" "),
            "https://thumbs.dreamstime.com/b/oak-tree-26681040.jpg", R.drawable.vk
        ),
        Post(
            "Хорошие мемы", "1 апреля 21:38",
            LoremIpsum(40).values.joinToString(" "),
            null, null
        ),
        Post(
            "Какой-то Пользователь", "2 апреля 4:31",
            LoremIpsum(4).values.joinToString(" "),
            "https://buffer.com/library/content/images/2023/10/free-images.jpg", R.drawable.vk,
            listOf(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ1I_6KKZO1P0yG0c27Qn312Lv4rxhChLHyB03pUmtDCQ&s",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQw4UeEjjERyEVTOIaXIKHlj7snPZAKulH5-z1Kau1lsw&s",
                "https://media.springernature.com/lw703/springer-static/image/art%3A10.1038%2F528452a/MediaObjects/41586_2015_Article_BF528452a_Figg_HTML.jpg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQtMBjxYzKSBNHKpa9pjiLcDdXxK_coI7_lcCqWTYMmwA&s",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS_Dn-wNSTpak7z_wC7ib3OmWLSQ6gY0dJUe8yhhSMUHw&s",
                "https://stimg.cardekho.com/images/carexteriorimages/930x620/Maruti/Jimny/6182/1686117643111/front-left-side-47.jpg?impolicy=resize&imwidth=420"
            )
        )
    ))
}

@Composable
fun NewsfeedScreen() {
    val viewModel = hiltViewModel<NewsfeedViewModel>()
    val pagingItems = viewModel.newsfeed.collectAsLazyPagingItems()

    LazyColumn {
        items(pagingItems.itemCount) {
            PostComponent(pagingItems[it])
        }
    }
}

@Composable
fun NewsfeedUi(posts: List<Post>) {
    LazyColumn {
        items(posts.size) {
            Spacer(modifier = Modifier.height(4.dp))
            PostComponent(posts[it])
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun PostComponent(post: Post) {
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
                Card(
                    modifier = Modifier.size(64.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    if (post.profileImage != null)
                        Picture(
                            modifier = Modifier.fillMaxSize(),
                            image = post.profileImage,
                            contentScale = ContentScale.Crop
                        )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = post.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            modifier = Modifier.size(24.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            if (post.networkIcon != null)
                                Picture(
                                    modifier = Modifier.fillMaxSize(),
                                    image = post.networkIcon
                                )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = post.datetime, color = MaterialTheme.colorScheme.outline)
                    }
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(R.drawable.ic_more), contentDescription = null)
                }
            }
            /* Body */
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = post.text)
            if (post.images.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                ImageAttachment(post.images)
            }
        }
    }
}