package com.hackaprende.dogedex.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hackaprende.dogedex.databinding.ActivityMainBinding
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
        initValidateUser()
        initListener()
    }

    private fun initListener() {
        binding.settingsFab.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun initValidateUser() {
        lifecycleScope.launch(Dispatchers.IO) {
            if (DataManager(applicationContext).getUser() == null) {
                logOutActivity()
            }
        }
    }

    private fun logOutActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}