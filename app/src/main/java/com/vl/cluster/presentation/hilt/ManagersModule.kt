package com.vl.cluster.presentation.hilt

import com.vl.cluster.domain.manager.AuthManager
import com.vl.cluster.domain.manager.NetworkReducer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManagersModule {
    @Provides
    @Singleton
    fun provideAuthManager(networks: NetworksWrapper) = AuthManager(networks.list)

    @Provides
    @Singleton
    fun provideReducer(networks: NetworksWrapper) = NetworkReducer(networks.list)
}