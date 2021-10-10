package net.dimterex.sync_client.di

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import net.dimterex.sync_client.data.ScopeFactory
import net.dimterex.sync_client.presenter.base.BasePresenter
import net.dimterex.sync_client.presenter.error.ErrorHandler
import org.kodein.di.Kodein
import org.kodein.di.erased.*

fun appModule(appContext: Context) = Kodein.Module(name = "app") {

    import(dataModule(appContext))

    bind<CoroutineScope>() with singleton {
        CoroutineScope(Dispatchers.Main + SupervisorJob())
    }

    bind<ErrorHandler>() with singleton { ErrorHandler.Impl() }

    bind<ScopeFactory>() with singleton { ScopeFactory.Impl() }

    import(executorsModule(appContext))
    import(repoModule)
}