package br.com.pluvia

import android.app.Application
import br.com.pluvia.data.remote.AuthTokenStore

class PluviaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AuthTokenStore.init(this)
    }
}
