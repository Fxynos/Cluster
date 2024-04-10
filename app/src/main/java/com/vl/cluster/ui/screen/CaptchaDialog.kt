package com.vl.cluster.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.vl.cluster.ui.theme.AppTheme

@Preview
@Composable
fun CaptchaDialogPreview() {
    AppTheme {
        val context = LocalContext.current
        var showDialog by remember { mutableStateOf(true) }
        if (showDialog) {
            CaptchaDialog(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS9BUtofO4eq0vJkPYWi8qDveUS7JHy5Aq3fVtLNszkuZi7GN7D5F8YlbIBWXoyt-LWUrw&usqp=CAU",
                onDone = {
                    showDialog = false
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaptchaDialog(imageUrl: String, onDone: (String) -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismiss) {
        Card {
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
                    imageUrl,
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
                    singleLine = true,
                    keyboardActions = KeyboardActions(onDone = { onDone(code) }),
                    keyboardOptions = KeyboardOptions(autoCorrect = false),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}