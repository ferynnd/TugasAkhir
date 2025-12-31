package dev.ferynnd.tugasakhir.data.helper

import android.content.Context
import android.content.SharedPreferences


class SharedPreferenceHelper(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "auth_pref"
        private const val KEY_IS_LOGIN = "is_login"
        private const val KEY_EMAIL = "email"
    }

    fun setLogin(isLogin: Boolean, email: String? = null) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGIN, isLogin)
            email?.let { putString(KEY_EMAIL, it) }
            apply()
        }
    }


    fun isLogin(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGIN, false)
    }

    fun getEmail(): String? {
        return prefs.getString(KEY_EMAIL, null)
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}
