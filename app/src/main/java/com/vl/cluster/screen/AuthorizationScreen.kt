package com.vl.cluster.screen

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import com.vl.cluster.api.definition.exception.ApiCustomException
import com.vl.cluster.api.definition.exception.CaptchaException
import com.vl.cluster.api.definition.exception.ConnectionException
import com.vl.cluster.api.definition.exception.TwoFaException
import com.vl.cluster.api.definition.exception.UnsupportedLoginMethodException
import com.vl.cluster.api.definition.feature.NetworkAuth
import com.vl.cluster.AuthViewModel
import com.vl.cluster.api.definition.exception.WrongCredentialsException
import com.vl.cluster.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

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
            val errorState = remember { mutableStateOf(false) }
            LoginScreen(
                viewModel = model,
                errorState,
                keyboardType = when (model.loginVariant) {
                    NetworkAuth.LoginType.PHONE -> KeyboardType.Phone
                    NetworkAuth.LoginType.EMAIL -> KeyboardType.Email
                    else -> KeyboardType.Text
                 },
                onDone = {
                    try {
                        model.attemptLogin()
                        errorState.value = false
                        navController.navigate(AuthRoute.PASSWORD.route) // TODO choose relevant sign method
                    } catch (e: AuthViewModel.MalformedInputException) {
                        errorState.value = true
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
            val errorState = remember { mutableStateOf(false) }
            var captcha: CaptchaException? by remember { mutableStateOf(null) }
            var customException: ApiCustomException? by remember { mutableStateOf(null) }
            val context = LocalContext.current
            fun toast(message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            /* Popups */
            if (customException != null)
                AlertDialog(
                    onDismissRequest = { customException = null },
                    title = { Text(text = customException!!.title) },
                    text = { Text(text = customException!!.description) },
                    confirmButton = {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { customException = null }
                        ) {
                            Text("Закрыть")
                        }
                    }
                )
            else if (captcha != null)
                CaptchaDialog(
                    imageUrl = captcha!!.url,
                    onDone = {
                        if (it.isBlank())
                            return@CaptchaDialog
                        try {
                            runBlocking(Dispatchers.IO) { // TODO refactor
                                captcha!!.confirm(it)
                            }
                            toast("Success ${
                                GlobalState.reducer.getSessions().last().run {"$sessionId $sessionName"}
                            }")
                        } catch (e: Exception) {
                            when (e) {
                                is WrongCredentialsException ->
                                    errorState.value = true
                                is CaptchaException -> captcha = e
                                is ConnectionException -> println("Connection error") // TODO connection error
                                is TwoFaException -> println("2FA required ${e.codeSource.name}") // TODO 2FA
                                is ApiCustomException -> customException = e
                                else -> throw e
                            }
                        }
                        captcha = null
                    },
                    onDismiss = {}
                )
            /* Screen */
            PasswordScreen(
                viewModel = model,
                errorState = errorState,
                onDone = {
                    try {
                        model.attemptPassword()
                        errorState.value = false
                        toast("Success ${
                                GlobalState.reducer.getSessions().last().run {"$sessionId $sessionName"}
                            }")
                    } catch (e: Exception) {
                        when (e) {
                            is AuthViewModel.MalformedInputException,
                            is WrongCredentialsException ->
                                errorState.value = true
                            is ConnectionException -> println("Connection error") // TODO connection error
                            is TwoFaException -> println("2FA required ${e.codeSource.name}") // TODO 2FA
                            is CaptchaException -> captcha = e
                            is UnsupportedLoginMethodException -> println("Login method is unsupported") // TODO unsupported login method
                            is ApiCustomException -> customException = e
                            else -> throw e
                        }
                    }
                }
            )
        }
    }

@Preview
@Composable
fun AuthPanelPreview(
    @PreviewParameter(NetworkPreviewParameterProvider::class) network: Network
) {
    AppTheme {
        Surface {
            val error = remember { mutableStateOf(false) }
            AuthPanel(
                network,
                "Логин",
                "Далее",
                remember { mutableStateOf("") },
                error,
                KeyboardType.Phone
            ) { error.value = !error.value }
        }
    }
}

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    errorState: MutableState<Boolean>,
    keyboardType: KeyboardType,
    onDone: (String) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AuthPanel(
            viewModel.network,
            "Логин",
            "Далее",
            viewModel.login,
            errorState,
            keyboardType,
            onDone
        )
    }
}

@Composable
fun PasswordScreen(viewModel: AuthViewModel, errorState: MutableState<Boolean>, onDone: (String) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AuthPanel(
            viewModel.network,
            "Пароль",
            "Войти",
            viewModel.password,
            errorState,
            KeyboardType.Password,
            onDone
        )
    }
}

@Composable
fun CodeScreen(viewModel: AuthViewModel, onDone: (String) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AuthPanel(
            viewModel.network,
            "Код из SMS",
            "Далее",
            remember { mutableStateOf("") }, // sms code
            remember { mutableStateOf(false) },
            KeyboardType.Number,
            onDone
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthPanel(
    network: Network,
    hint: String,
    buttonText: String,
    inputTextState: MutableState<String>,
    errorState: MutableState<Boolean>,
    keyboardType: KeyboardType,
    onDone: (String) -> Unit
) {
    var inputText by inputTextState
    val error by errorState
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
                textColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.inversePrimary
            ),
            label = { Text(text = hint, color = MaterialTheme.colorScheme.primary) },
            //placeholder = { Text(text = hint, color = MaterialTheme.colorScheme.onSurface) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = { onDone(inputText) }),
            keyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = keyboardType),
            visualTransformation = if (keyboardType == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None,
            isError = error
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