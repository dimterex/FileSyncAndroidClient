package net.dimterex.sync_client.di

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import net.dimterex.sync_client.presenter.base.BasePresenter
import net.dimterex.sync_client.presenter.error.ErrorHandler
import net.dimterex.sync_client.presenter.executor.UseCaseExecutor
import org.kodein.di.Kodein
import org.kodein.di.erased.*

fun appModule(appContext: Context) = Kodein.Module(name = "app") {

    import(dataModule(appContext))

    bind<CoroutineScope>() with singleton {
        CoroutineScope(Dispatchers.Main + SupervisorJob())
    }

    bind<ErrorHandler>() with singleton { ErrorHandler.Impl() }

    bind<UseCaseExecutor>() with multiton {presenter: BasePresenter ->
        UseCaseExecutor(instance(), presenter::onError, instance())
    }

    import(repoModule)
}