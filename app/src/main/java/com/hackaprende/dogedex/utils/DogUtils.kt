package com.hackaprende.dogedex.utils

import android.util.Patterns

class DogUtils {
    companion object {
        fun isValidEmail(email: String?): Boolean {
            return !email.isNullOrEmpty() &&
                    Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

    }
}