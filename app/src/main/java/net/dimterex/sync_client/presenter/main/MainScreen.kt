package net.dimterex.sync_client.presenter.main

import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.dimterex.sync_client.modules.ApiModules
import net.dimterex.sync_client.modules.ConnectionManager
import net.dimterex.sync_client.modules.SettingsManager
import net.dimterex.sync_client.presenter.base.BasePresenter
import net.dimterex.sync_client.presenter.base.BaseView
import org.kodein.di.erased.instance

class MainPresenter(private val view: MainView) : BasePresenter(view)
{
    private val _apiModules by instance<ApiModules>()
    private val _settings by instance<SettingsManager>()


    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)
        _settings.initialize()
    }
}

interface MainView : BaseView {}