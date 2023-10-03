package com.vl.cluster.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.navigation
import com.vl.cluster.R
import com.vl.cluster.ui.theme.AppTheme

fun NavGraphBuilder.authorizationNavigation(
    navController: NavController,
    route: String
) = navigation(
        route = route,
        startDestination = "login?networkName={networkName}&networkIcon={networkIcon}",
        arguments = listOf(
            navArgument("networkName") {
                nullable = false
                type = NavType.StringType
            },
            navArgument("networkIcon") {
                nullable = false
                type = NavType.IntType
            }
        )
    ) {
        composable(
            route = "login?networkName={networkName}&networkIcon={networkIcon}",
            arguments = listOf(
                navArgument("networkName") {
                    nullable = false
                    type = NavType.StringType
                },
                navArgument("networkIcon") {
                    nullable = false
                    type = NavType.IntType
                }
            )
        ) { backStack ->
            LoginScreen(backStack.arguments!!.run {
                Network(getString("networkName")!!, getInt("networkIcon"))
            })
        }
    }

@Preview
@Composable
fun LoginScreenPreview(@PreviewParameter(NetworkPreviewParameterProvider::class) network: Network) {
    AppTheme {
        Surface {
            LoginScreen(network)
        }
    }
}

@Composable
fun LoginScreen(network: Network) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AuthPanel(network)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthPanel(network: Network) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Вход",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = network.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(4.dp))
                Image(
                    painter = painterResource(network.icon),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        var text by remember { mutableStateOf("") }
        TextField(
            value = text,
            onValueChange = { text = it },
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.inversePrimary
            ),
            placeholder = { Text(text = "Логин", color = MaterialTheme.colorScheme.onSurface) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {}
        ) {
            Text("Далее")
        }
    }
}

class NetworkPreviewParameterProvider: PreviewParameterProvider<Network> {
    override val values = sequenceOf(Network("ВКонтакте", R.drawable.vk))
}