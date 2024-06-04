package com.vl.cluster.presentation.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import com.vl.cluster.R
import com.vl.cluster.presentation.entity.NetworkData
import com.vl.cluster.presentation.theme.AppTheme

private const val EMAIL_CONTACT = "sinerechka0000@gmail.com"
private const val EMAIL_SUBJECT = "Cluster Feedback"
private val EMAIL_TEMPLATE = """
        Привет! 
        Мне интересно твоё приложение, потому что у меня каждый день возникает путаница с диалогами в соцсетях.
        В первую очередь я жду функцию агрегации диалогов.
        Из мессенджеров мне нужны только VK и Telegram.
""".trimIndent()

@Preview
@Composable
fun NetworksScreenPreview(
    @PreviewParameter(NetworksPreviewParameterProvider::class)
    networks: List<NetworkData>
) {
    AppTheme {
        Surface {
            NetworksScreen(networks)
        }
    }
}

@Composable
fun NetworksScreen(
    networks: List<NetworkData>,
    onClick: (NetworkData) -> Unit = {}
) {
    val context = LocalContext.current
    var isDialogShown by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        for (network in networks) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 2.dp),
                shape = RoundedCornerShape(25),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                onClick = { onClick(network) }
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.fillMaxWidth()
                            .wrapContentWidth(align = Alignment.Start)
                            .size(48.dp),
                        painter = painterResource(network.icon),
                        contentDescription = null
                    )
                    Text(text = network.name)
                }
            }
        }
    }

    if (isDialogShown)
        AlertDialog(
            title = {
                Text(
                    text = context.getString(R.string.apologize_title),
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                val text = context.getString(R.string.apologize_text, EMAIL_CONTACT)
                val start = text.indexOf(EMAIL_CONTACT)
                val end = start + EMAIL_CONTACT.length

                ClickableText(
                    text = AnnotatedString.Builder(text).apply {
                        addStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = Color.Blue
                            ), start, end
                        )
                    }.toAnnotatedString(),
                    onClick = { position ->
                        if (position in start until end)
                            context.startActivity(Intent.createChooser(
                                Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:")).apply {
                                    putExtra(Intent.EXTRA_EMAIL, arrayOf(EMAIL_CONTACT))
                                    putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT)
                                    putExtra(Intent.EXTRA_TEXT, EMAIL_TEMPLATE)
                                },
                                context.getString(R.string.send_email)
                            ))
                    }
                )
            },
            onDismissRequest = {},
            confirmButton = {
                Button(onClick = { isDialogShown = false }) {
                    Text(text = context.getString(R.string.apologize_button))
                }
            }
        )
}

class NetworksPreviewParameterProvider: PreviewParameterProvider<List<NetworkData>> {
    override val values: Sequence<List<NetworkData>> = sequenceOf(listOf(
        NetworkData("ВКонтакте", 0, R.drawable.vk),
        NetworkData("Telegram", 0, R.drawable.telegram),
        NetworkData("Debug", 0, 0)
    ))

    override val count: Int
        get() = values.count()
}