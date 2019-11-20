package net.dimterex.sync_client.presenter.main

import android.os.Bundle
import net.dimterex.sync_client.modules.ApiModules
import net.dimterex.sync_client.modules.ConnectionManager
import net.dimterex.sync_client.presenter.base.BasePresenter
import net.dimterex.sync_client.presenter.base.BaseView
import net.dimterex.sync_client.usecase.repos.ISettingsSource
import org.kodein.di.erased.instance
import kotlin.concurrent.thread

class MainPresenter(private val view: MainView) : BasePresenter(view)
{
    private val _apiModules by instance<ApiModules>()
    private val _connection by instance<ConnectionManager>()
    private val _repoSource by instance<ISettingsSource>()

    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)

        var tt = Thread.currentThread();
        thread (start = true) {
            val threadId = Thread.currentThread().id
            println("THREAD start connetion ${threadId}")
            _repoSource.load_settings()
            _apiModules.initialize()
            tt.runCatching {
                _connection.start()
            }
        }
    }
}

interface MainView : BaseView {}