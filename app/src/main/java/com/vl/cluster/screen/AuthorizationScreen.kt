package com.vl.cluster.screen

import android.annotation.SuppressLint
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.navigation
import com.vl.cluster.GlobalState
import com.vl.cluster.GlobalState.getIcon
import com.vl.cluster.R
import com.vl.cluster.api.definition.exception.CaptchaException
import com.vl.cluster.api.definition.exception.ConnectionException
import com.vl.cluster.api.definition.exception.TwoFaException
import com.vl.cluster.api.definition.exception.UnsupportedLoginMethodException
import com.vl.cluster.api.definition.features.NetworkAuth
import com.vl.cluster.logic.AuthViewModel
import com.vl.cluster.ui.theme.AppTheme

@SuppressLint("UnrememberedGetBackStackEntry")
fun NavGraphBuilder.authorizationNavigation(
    navController: NavController,
    route: String
) = navigation(
        route = route,
        startDestination = AuthRoute.LOGIN.route,
        arguments = AuthRoute.LOGIN.args
    ) {
        composable(
            route = AuthRoute.LOGIN.route,
            arguments = AuthRoute.LOGIN.args
        ) { backStack ->
            val model = viewModel<AuthViewModel>()
            model.network = GlobalState.reducer
                .findNetById(backStack.arguments!!.getInt("networkId")).let {
                    Network(it.networkName, it.networkId, it.getIcon())
                }
            LoginScreen(
                viewModel = model,
                onDone = {
                    try {
                        model.attemptLogin()
                        navController.navigate(AuthRoute.PASSWORD.route) // TODO choose relevant sign method
                    } catch (e: AuthViewModel.MalformedInputException) {
                        println("Invalid input") // TODO error state
                    }
                }
            )
        }
        composable(
            route = AuthRoute.PASSWORD.route,
            arguments = AuthRoute.PASSWORD.args
        ) {
            val model = viewModel<AuthViewModel>(
                remember { navController.getBackStackEntry(AuthRoute.LOGIN.route) }
            )
            PasswordScreen(
                viewModel = model,
                onDone = {
                    try {
                        model.attemptPassword()
                        println("Success ${GlobalState.reducer.getSessions().last().run {"$sessionId $sessionName"}}")
                    } catch (e: AuthViewModel.MalformedInputException) {
                        println("Malformed input") // TODO error state
                    } catch (e: NetworkAuth.Password.WrongCredentialsException) {
                        println("Wrong credentials") // TODO wrong credentials
                    } catch (e: ConnectionException) {
                        println("Connection error") // TODO connection error
                    } catch (e: TwoFaException) {
                        println("2FA required ${e.codeSource.name}") // TODO 2FA
                    } catch (e: CaptchaException) {
                        println("Captcha ${e.url}") // TODO wrong credentials
                    } catch (e: UnsupportedLoginMethodException) {
                        println("Login method is unsupported") // TODO unsupported login method
                    }
                }
            )
        }
    }

@Preview
@Composable
fun LoginScreenPreview(@PreviewParameter(NetworkPreviewParameterProvider::class) network: Network) {
    AppTheme {
        Surface {
            LoginScreen(
                viewModel = viewModel<AuthViewModel>()
                    .also { it.network = network },
                onDone = {}
            )
        }
    }
}

@Composable
fun LoginScreen(viewModel: AuthViewModel, onDone: (String) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AuthPanel(viewModel.network, "Логин", "Далее", viewModel.login, onDone)
    }
}

@Composable
fun PasswordScreen(viewModel: AuthViewModel, onDone: (String) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AuthPanel(viewModel.network, "Пароль", "Войти", viewModel.password, onDone)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthPanel(
    network: Network,
    hint: String,
    buttonText: String,
    inputTextState: MutableState<String>,
    onDone: (String) -> Unit
) {
    var inputText by remember { inputTextState }
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
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(top = 4.dp, start = 8.dp)
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
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.inversePrimary
            ),
            placeholder = { Text(text = hint, color = MaterialTheme.colorScheme.onSurface) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = { onDone(inputText) })
        )
        /*BasicTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(text)
        }*/
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onDone(inputText) }
        ) {
            Text(buttonText)
        }
    }
}

class NetworkPreviewParameterProvider: PreviewParameterProvider<Network> {
    override val values = sequenceOf(Network("ВКонтакте", 0, R.drawable.vk))
}

private enum class AuthRoute(val route: String, val args: List<NamedNavArgument>) {
    LOGIN(
        "login?networkId={networkId}",
        listOf(
            navArgument("networkId") {
                nullable = false
                type = NavType.IntType
            }
        )
    ),
    PASSWORD("password", listOf())
}