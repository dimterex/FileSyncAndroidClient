package net.dimterex.sync_client

import android.app.Application
import net.dimterex.sync_client.di.appModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class App : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        import(appModule(this@App))
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        var INSTANCE: App? = null
        fun kodein() = INSTANCE!!.kodein
    }
}