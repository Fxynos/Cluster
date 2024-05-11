package com.vl.cluster.presentation.screen

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.navigation
import com.vl.cluster.R
import com.vl.cluster.domain.entity.LoginType
import com.vl.cluster.presentation.viewmodel.AuthViewModel
import com.vl.cluster.presentation.entity.NetworkData
import com.vl.cluster.presentation.theme.AppTheme

@SuppressLint("UnrememberedGetBackStackEntry")
fun NavGraphBuilder.authorizationNavigation(
    navController: NavController,
    route: String,
    onAuthenticated: () -> Unit
) = navigation(
        route = route,
        startDestination = AuthRoute.LOGIN.route,
        arguments = AuthRoute.LOGIN.args
    ) {
        composable(
            route = AuthRoute.LOGIN.route,
            arguments = AuthRoute.LOGIN.args
        ) { backStack ->
            val model = hiltViewModel<AuthViewModel, AuthViewModel.Factory> { factory ->
                factory.create(backStack.arguments!!.getInt("networkId"))
            }
            val uiState by model.uiState.collectAsState()

            Popups(uiState, model)
            LoginScreen(
                viewModel = model,
                error = (uiState as? AuthViewModel.UiState.Error)?.hint,
                isProcessing = uiState == AuthViewModel.UiState.Processing,
                keyboardType = when (model.loginVariant) {
                    LoginType.PHONE -> KeyboardType.Phone
                    LoginType.EMAIL -> KeyboardType.Email
                    else -> KeyboardType.Text
                 },
                onRetry = model::retry,
                onDone = {
                    model.attemptLogin(it) // synchronously check if login matches regex
                    if (model.uiState.value !is AuthViewModel.UiState.Error)
                        navController.navigate(AuthRoute.PASSWORD.route) // TODO choose relevant sign method
                }
            )
        }
        composable(
            route = AuthRoute.PASSWORD.route,
            arguments = AuthRoute.PASSWORD.args
        ) {
            val model = hiltViewModel<AuthViewModel>(
                navController.getBackStackEntry(AuthRoute.LOGIN.route)
            )
            val uiState by model.uiState.collectAsState()

            LaunchedEffect(uiState) {
                if (uiState is AuthViewModel.UiState.Authenticated)
                    onAuthenticated()
            }

            Popups(uiState, model)
            PasswordScreen(
                viewModel = model,
                error = (uiState as? AuthViewModel.UiState.Error)?.hint,
                isProcessing = uiState == AuthViewModel.UiState.Processing,
                onRetry = model::retry,
                onDone = {
                    if (uiState != AuthViewModel.UiState.Processing)
                        model.attemptPassword(it)
                }
            )
        }
    }

@Composable
fun Popups(uiState: AuthViewModel.UiState, model: AuthViewModel) {
    Popup(uiState) { model.retry() }
    Captcha(uiState) {
        if (it == null)
            model.retry()
        else if (uiState != AuthViewModel.UiState.Processing)
            model.attemptCaptcha(it)
    }
}

@Composable
fun Popup(uiState: AuthViewModel.UiState, onCancel: () -> Unit) {
    if (uiState is AuthViewModel.UiState.Popup)
        AlertDialog(
            onDismissRequest = onCancel,
            title = { Text(uiState.title) },
            text = { Text(uiState.description) },
            confirmButton = {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCancel
                ) {
                    Text(LocalContext.current.getString(R.string.close))
                }
            }
        )
}

@Composable
fun Captcha(uiState: AuthViewModel.UiState, onConfirm: (String?) -> Unit) {
    if (uiState is AuthViewModel.UiState.Captcha)
        CaptchaDialog(
            imageUrl = uiState.imageUrl,
            onDone = onConfirm,
            onDismiss = { onConfirm(null) }
        )
}

@Preview
@Composable
fun AuthPanelPreview(
    @PreviewParameter(NetworkPreviewParameterProvider::class) network: NetworkData
) {
    AppTheme {
        Surface {
            AuthPanel(
                network,
                "Логин",
                "Далее",
                remember { mutableStateOf("") },
                null,
                true,
                KeyboardType.Phone,
                {}, {}
            )
        }
    }
}

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    error: String?,
    isProcessing: Boolean,
    keyboardType: KeyboardType,
    onRetry: () -> Unit,
    onDone: (String) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AuthPanel(
            viewModel.networkData,
            LocalContext.current.getString(R.string.login),
            LocalContext.current.getString(R.string.next),
            remember { mutableStateOf("") },
            error,
            isProcessing,
            keyboardType,
            onRetry,
            onDone
        )
    }
}

@Composable
fun PasswordScreen(
    viewModel: AuthViewModel,
    error: String?,
    isProcessing: Boolean,
    onRetry: () -> Unit,
    onDone: (String) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AuthPanel(
            viewModel.networkData,
            LocalContext.current.getString(R.string.password),
            LocalContext.current.getString(R.string.sign_in),
            remember { mutableStateOf("") },
            error,
            isProcessing,
            KeyboardType.Password,
            onRetry,
            onDone
        )
    }
}

@Composable
fun CodeScreen(
    viewModel: AuthViewModel,
    error: String?,
    isProcessing: Boolean,
    onRetry: () -> Unit,
    onDone: (String) -> Unit
) { // TODO code auth
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AuthPanel(
            viewModel.networkData,
            "Код из SMS",
            "Далее",
            remember { mutableStateOf("") }, // sms code
            error,
            isProcessing,
            KeyboardType.Number,
            onRetry,
            onDone
        )
    }
}

/**
 * @param onRetry hide supporting text, cancel error
 */
@Composable
fun AuthPanel(
    network: NetworkData,
    hint: String,
    buttonText: String,
    inputTextState: MutableState<String>,
    error: String?,
    isProcessing: Boolean,
    keyboardType: KeyboardType,
    onRetry: () -> Unit,
    onDone: (String) -> Unit
) {
    var inputText by inputTextState

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
                text = LocalContext.current.getString(R.string.authentication),
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
            onValueChange = {
                if (error != null)
                    onRetry()
                inputText = it
            },
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.inversePrimary
            ),
            label = { Text(text = hint, color = MaterialTheme.colorScheme.primary) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = { onDone(inputText) }),
            keyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = keyboardType),
            visualTransformation = if (keyboardType == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None,
            isError = error != null,
            supportingText = { error?.let { Text(it, color = MaterialTheme.colorScheme.onErrorContainer) } }
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { if (!isProcessing) onDone(inputText) },
            enabled = !isProcessing
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(32.dp)) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = buttonText
                )
                if (isProcessing)
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterEnd)
                    )
            }
        }
    }
}

class NetworkPreviewParameterProvider: PreviewParameterProvider<NetworkData> {
    override val values = sequenceOf(NetworkData("ВКонтакте", 0, R.drawable.vk))
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