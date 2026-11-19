package br.com.pluvia.data.remote

import android.content.Context
import android.content.SharedPreferences

object AuthTokenStore {
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences("pluvia_auth", Context.MODE_PRIVATE)
    }

    var token: String?
        get() = if (::prefs.isInitialized) prefs.getString("token", null) else null
        set(value) {
            if (::prefs.isInitialized) {
                prefs.edit().putString("token", value).apply()
            }
        }

    fun clear() = token?.let { token = null }
}
