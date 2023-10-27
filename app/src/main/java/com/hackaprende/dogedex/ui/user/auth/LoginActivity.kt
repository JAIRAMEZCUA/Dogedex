package com.hackaprende.dogedex.ui.user.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.hackaprende.dogedex.R
import com.hackaprende.dogedex.data.network.api.sealed.ApiResponseStatusGeneric
import com.hackaprende.dogedex.databinding.ActivityLoginBinding
import com.hackaprende.dogedex.ui.MainActivity
import com.hackaprende.dogedex.ui.user.auth.viewModel.LoginActivityViewModel
import com.hackaprende.dogedex.ui.user.auth.viewModel.ViewModelFactoryAuth
import com.hackaprende.dogedex.utils.visible

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var loginActivityViewModel: LoginActivityViewModel? = null
    private val statusObserver: LoginActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUIState()
        initViewModel()
    }

    private fun initViewModel() {
        loginActivityViewModel = ViewModelProvider(
            viewModelStore,
            ViewModelFactoryAuth()
        )[LoginActivityViewModel::class.java]
    }

    private fun initUIState() {
        statusObserver.status.observe(this) {
            when (it) {
                is ApiResponseStatusGeneric.ERROR -> statusError(it.error)
                is ApiResponseStatusGeneric.Loading -> statusLoading()
                is ApiResponseStatusGeneric.SUCCESS -> statusSuccess()
            }
        }
        statusObserver.user.observe(this) { user ->
            if (user != null) {
                startMainActivity()
            }
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
//        findNavController(R.id.nav_host_fragment).navigate(
//            LoginActivityDirections.actionLoginActivityToMainActivity()
//        )
    }

    private fun statusSuccess() {
        binding.loadingWheel.visible(false)
    }

    private fun statusLoading() {
        binding.loadingWheel.visible(true)
    }

    private fun statusError(status: Int) {
        binding.loadingWheel.visible(false)
        showErrorDialog(status)
    }

    private fun showErrorDialog(messageId: Int) {
        AlertDialog.Builder(this)
            .setTitle(R.string.there_was_an_error)
            .setMessage(messageId)
            .setPositiveButton(android.R.string.ok) { _, _ -> /** Dismiss dialog **/ }
            .create()
            .show()
    }
}