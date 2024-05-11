package com.vl.cluster.presentation.screen

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.vl.cluster.R
import com.vl.cluster.presentation.entity.NetworkData
import com.vl.cluster.presentation.theme.AppTheme

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