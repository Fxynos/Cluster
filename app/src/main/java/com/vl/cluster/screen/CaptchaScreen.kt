package com.vl.cluster.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vl.cluster.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CaptchaScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AppTheme {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .background(
                        MaterialTheme.colorScheme.background,
                        RoundedCornerShape(12.dp)
                    )
            ) {
                var code by remember { mutableStateOf("") }
                AsyncImage(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS9BUtofO4eq0vJkPYWi8qDveUS7JHy5Aq3fVtLNszkuZi7GN7D5F8YlbIBWXoyt-LWUrw&usqp=CAU",
                    null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
                TextField(
                    value = code,
                    onValueChange = { code = it },
                    placeholder = { Text("Текст на картинке") },
                    modifier = Modifier.fillMaxWidth()
                        .padding(12.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}