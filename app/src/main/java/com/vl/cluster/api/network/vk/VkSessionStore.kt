package com.vl.cluster.api.network.vk

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.vl.cluster.api.definition.Session
import com.vl.cluster.api.definition.SessionStore
import com.vl.cluster.api.network.vk.VkNetwork.VkSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class VkSessionStore(private val context: Context, private val network: VkNetwork): SessionStore {
    companion object {
        private val sessionsKey: Preferences.Key<Set<String>> = stringSetPreferencesKey("sessions")
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("VK")
        private val gson = Gson()
    }

    private val dataStore: DataStore<Preferences>
        get() = context.dataStore

    private val coroutineScope = CoroutineScope(Job())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val sessions: StateFlow<Set<VkSession>> = runBlocking {
        dataStore.data.transformLatest { prefs ->
            emit(prefs[sessionsKey]?.map { s ->
                val entity = gson.fromJson(s, SessionEntity::class.java)
                network.VkSession(entity.userId, entity.token)
            }?.toSet() ?: setOf())
        }.stateIn(coroutineScope)
    }

    override fun getSessions(): Set<Session> = sessions.value

    override fun updateSessions(sessions: Set<Session>) {
        coroutineScope.launch(Dispatchers.IO) {
            dataStore.edit { prefs ->
                prefs[sessionsKey] = sessions.map { gson.toJson(it) }.toSet()
            }
        }
    }

    private class SessionEntity(val userId: Int, val token: String)
}