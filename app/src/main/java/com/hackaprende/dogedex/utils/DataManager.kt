package com.hackaprende.dogedex.utils

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.datastore.preferences.remove
import com.google.gson.Gson
import com.hackaprende.dogedex.data.network.api.models.User
import kotlinx.coroutines.flow.first

class DataManager(context: Context) {
    private val dataStore = context.createDataStore(USER_PREFERENCES_NAME)
    private val gson = Gson()

    companion object {
        val USER_KEY = preferencesKey<String>(USER_KEY_JSON)
    }

    suspend fun saveUser(user: User) {
        val jsonString = gson.toJson(user)
        dataStore.edit { preferences ->
            preferences[USER_KEY] = jsonString
        }
    }

    suspend fun getUser(): User? {
        val preferencesData = dataStore.data.first()
        val jsonString = preferencesData[USER_KEY] ?: return null
        return gson.fromJson(jsonString, User::class.java)
    }

    suspend fun deleteUser() {
        dataStore.edit { preferences ->
            preferences.remove(USER_KEY)
        }
    }
}