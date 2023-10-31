package com.hackaprende.dogedex.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.hackaprende.dogedex.R
import com.hackaprende.dogedex.data.network.api.models.User
import com.hackaprende.dogedex.ui.user.auth.LoginActivity
import com.hackaprende.dogedex.utils.KEY_EMAIL
import com.hackaprende.dogedex.utils.KEY_ID
import com.hackaprende.dogedex.utils.KEY_TOKEN
import com.hackaprende.dogedex.utils.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launch(Dispatchers.IO) {
            getUser().collect { user ->
                if (validateUserInvalid(user)) {
                    logOutActivity()
                }
                withContext(Dispatchers.Main) {
                    //UI
                }
            }
        }
    }


    private fun validateUserInvalid(user: User): Boolean {
        return (user.id.equals(0L) || user.email.isEmpty() || user.authenticationToken.isEmpty())
    }

    private fun logOutActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun getUser() = dataStore.data.map { pref ->
        User(
            id = pref[longPreferencesKey(KEY_ID)] ?: 0,
            authenticationToken = pref[stringPreferencesKey(KEY_TOKEN)] ?: "",
            email = pref[stringPreferencesKey(KEY_EMAIL)] ?: ""
        )
    }
}