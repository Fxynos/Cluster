package com.vl.cluster.presentation.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.graphics.Color
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
import com.vl.cluster.presentation.theme.AppTheme
import com.vl.cluster.presentation.viewmodel.NewsfeedViewModel

@Preview(showSystemUi = true)
@Composable
fun NewsfeedScreenPreview() {
    AppTheme {
        Surface {
            NewsfeedUi(listOf(
                Post(
                    id = "id1",
                    title = "Какой-то Пользователь",
                    datetime = "2 апреля 4:31",
                    text = LoremIpsum(4).values.joinToString(" "),
                    profileImage = "https://buffer.com/library/content/images/2023/10/free-images.jpg", R.drawable.vk,
                    likesCount = 5,
                    commentsCount = 1,
                    repostsCount = 0,
                    hasLike = true,
                    images = listOf(
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ1I_6KKZO1P0yG0c27Qn312Lv4rxhChLHyB03pUmtDCQ&s",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQw4UeEjjERyEVTOIaXIKHlj7snPZAKulH5-z1Kau1lsw&s",
                        "https://media.springernature.com/lw703/springer-static/image/art%3A10.1038%2F528452a/MediaObjects/41586_2015_Article_BF528452a_Figg_HTML.jpg",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQtMBjxYzKSBNHKpa9pjiLcDdXxK_coI7_lcCqWTYMmwA&s",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS_Dn-wNSTpak7z_wC7ib3OmWLSQ6gY0dJUe8yhhSMUHw&s",
                        "https://stimg.cardekho.com/images/carexteriorimages/930x620/Maruti/Jimny/6182/1686117643111/front-left-side-47.jpg?impolicy=resize&imwidth=420"
                    )
                ),
                Post(
                    id = "id2",
                    title = "Мемов Нет",
                    datetime = "сегодня 15:31",
                    text = "Здесь бы был текст",
                    profileImage = null,
                    networkIcon = null,
                    likesCount = 0,
                    commentsCount = 0,
                    repostsCount = 0,
                    hasLike = false
                )
            ))
        }
    }
}

@Composable
fun NewsfeedScreen() {
    val viewModel = hiltViewModel<NewsfeedViewModel>()
    val pagingItems = viewModel.newsfeed.collectAsLazyPagingItems()

    LazyColumn {
        items(pagingItems.itemCount) {
            Spacer(modifier = Modifier.height(4.dp))
            PostComponent(pagingItems[it]!!)
            Spacer(modifier = Modifier.height(4.dp))
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
            /* Footer */
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                PostButton( // like
                    text = post.likesCount.toString(),
                    icon = if (post.hasLike) R.drawable.ic_like else R.drawable.ic_like_border,
                    isActive = post.hasLike,
                    onClick = { /*TODO*/ }
                )
                Spacer(modifier = Modifier.width(8.dp))
                PostButton( // comment
                    text = post.commentsCount.toString(),
                    icon = R.drawable.ic_comment,
                    onClick = { /*TODO*/ }
                )
                Spacer(modifier = Modifier.width(8.dp))
                PostButton( // repost
                    text = post.repostsCount.toString(),
                    icon = R.drawable.ic_repost,
                    onClick = { /*TODO*/ }
                )
            }
        }
    }
}

@Composable
fun PostButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    @DrawableRes icon: Int,
    isActive: Boolean = false
) {
    val (containerColor, contentColor) =
        if (isActive)
            Color(0xFF, 0xDD, 0xDD) to Color.Red
        else
            MaterialTheme.colorScheme.surfaceContainerLow to MaterialTheme.colorScheme.outline
    Button(
        colors = ButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor,
            disabledContentColor = contentColor
        ),
        onClick = onClick
    ) {
        Icon(painter = painterResource(icon), contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}