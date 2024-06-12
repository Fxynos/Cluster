package com.vl.cluster.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vl.cluster.R
import com.vl.cluster.data.network.vk.VkNetwork
import com.vl.cluster.domain.manager.AuthManager
import com.vl.cluster.presentation.entity.NetworkData
import com.vl.cluster.presentation.screen.NetworksScreen
import com.vl.cluster.presentation.screen.WelcomeSliderPage
import com.vl.cluster.presentation.screen.WelcomeSliderScreen
import com.vl.cluster.presentation.screen.authorizationNavigation
import com.vl.cluster.presentation.screen.MenuScreen
import com.vl.cluster.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    @Inject lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isAuthorized = authManager.isAuthorized
        setContent {
            AppTheme(useDarkTheme = false) {
                Surface {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = if (isAuthorized) "menu" else "welcomeScreen"
                    ) {
                        composable("welcomeScreen") {
                            WelcomeSliderScreen(listOf(
                                WelcomeSliderPage(
                                    "Быстро и удобно",
                                    "Быстрый доступ к перепискам и файлам в одном месте.",
                                    R.drawable.slider_clock
                                ),
                                WelcomeSliderPage(
                                    "Просто и безопасно",
                                    "Все данные хранятся на вашем устройстве - мы не имеем к ним доступ.",
                                    R.drawable.slider_lock
                                ),
                                WelcomeSliderPage(
                                    "Будьте в курсе последних событий",
                                    "Новости из ваших сообществ собраны в единую ленту - вам больше не нужно постоянно переключаться между множеством приложений.",
                                    R.drawable.slider_meeting_info
                                )
                            ),
                                onAddAccountClick = {
                                    navController.navigate("networks")
                                }
                            )
                        }
                        composable("networks") {
                            NetworksScreen(
                                authManager.networks
                                    .filter { it.networkId == VkNetwork.NETWORK_ID }
                                    .map { NetworkData(it.networkName, it.networkId, it.icon) },
                                onClick = { network ->
                                    navController.navigate(
                                        "authorization?networkId=${network.id}"
                                    )
                                }
                            )
                        }
                        authorizationNavigation(
                            navController = navController,
                            route = "authorization?networkId={networkId}",
                            onAuthenticated = { navController.navigate("menu") }
                        )
                        composable("menu") { MenuScreen() }
                    }
                }
            }
        }
    }
}