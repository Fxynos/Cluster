package com.vl.cluster

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.core.os.bundleOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vl.cluster.screen.Network
import com.vl.cluster.screen.NetworksScreen
import com.vl.cluster.screen.WelcomeSliderPage
import com.vl.cluster.screen.WelcomeSliderScreen
import com.vl.cluster.screen.authorizationNavigation
import com.vl.cluster.ui.theme.AppTheme

class SingleAppActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "welcomeScreen"
                    ) {
                        composable("welcomeScreen") {
                            WelcomeSliderScreen(listOf( // demo
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
                                listOf(
                                    Network("ВКонтакте", R.drawable.vk),
                                    Network("Telegram", R.drawable.telegram)
                                ),
                                onClick = { network ->
                                    navController.navigate(
                                        "authorization?networkName=${Uri.encode(network.name)}&networkIcon=${network.icon}"
                                    )
                                }
                            )
                        }
                        authorizationNavigation(
                            navController,
                            "authorization?networkName={networkName}&networkIcon={networkIcon}"
                        )
                    }
                }
            }
        }
    }
}