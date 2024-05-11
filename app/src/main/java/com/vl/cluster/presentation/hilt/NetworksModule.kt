package com.vl.cluster.presentation.hilt

import android.content.Context
import com.vl.cluster.data.network.tg.TelegramNetwork
import com.vl.cluster.data.network.vk.VkNetwork
import com.vl.cluster.domain.boundary.Network
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworksModule {
    @Provides
    @Singleton
    fun provideVkNetwork(@ApplicationContext context: Context) = VkNetwork(context)

    @Provides
    @Singleton
    fun provideTelegramNetwork(@ApplicationContext context: Context) = TelegramNetwork(context)

    @Provides
    @Singleton
    fun provideNetworks(vk: VkNetwork, tg: TelegramNetwork): NetworksWrapper = NetworksWrapper(vk, tg)
}