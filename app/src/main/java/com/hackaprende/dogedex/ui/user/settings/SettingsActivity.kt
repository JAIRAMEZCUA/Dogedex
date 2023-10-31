package com.hackaprende.dogedex.ui.user.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hackaprende.dogedex.databinding.ActivitySettingsBinding
import com.hackaprende.dogedex.ui.user.auth.LoginActivity
import com.hackaprende.dogedex.utils.DataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
    }

    private fun initListeners() {
        binding.logoutButton.setOnClickListener {
            deleteUser()
            logOutActivity()
        }
    }

    private fun logOutActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        //TODO con esta bandera se limpia el cache de activitys y lo que sirve es que al hacer back hacia atras se cierre la app
        //y no nos regrese a una activityAnterior
    }

    private fun deleteUser() {
        lifecycleScope.launch(Dispatchers.IO) {
            DataManager(applicationContext).deleteUser()
        }
    }
}