package com.hackaprende.dogedex.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hackaprende.dogedex.data.network.api.ApiServiceInterceptor
import com.hackaprende.dogedex.databinding.ActivityMainBinding
import com.hackaprende.dogedex.ui.dog.dogList.DogListActivity
import com.hackaprende.dogedex.ui.user.auth.LoginActivity
import com.hackaprende.dogedex.ui.user.settings.SettingsActivity
import com.hackaprende.dogedex.utils.DataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        verifyUserSignIn()
        initListener()
    }

    private fun initListener() {
        binding.settingsFab.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        binding.dogListFab.setOnClickListener {
            openDogListActivity()
        }
    }

    private fun verifyUserSignIn() {
        lifecycleScope.launch(Dispatchers.IO) {
            val user = DataManager(applicationContext).getUser()
            if (user == null) {
                logOutActivity()
            } else {
                ApiServiceInterceptor.setSessionToken(user.authenticationToken)
            }
        }
    }

    private fun openDogListActivity() {
        startActivity(Intent(this, DogListActivity::class.java))
    }

    private fun logOutActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}