package br.com.pluvia

import android.app.Application
import br.com.pluvia.data.remote.MockDataStore

class PluviaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MockDataStore.init(this)
    }
}
