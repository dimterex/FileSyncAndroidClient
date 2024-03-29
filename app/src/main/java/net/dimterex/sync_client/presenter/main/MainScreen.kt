package net.dimterex.sync_client.presenter.main

import android.os.Bundle
import net.dimterex.sync_client.api.Message.Connection.ConnectionRequest
import net.dimterex.sync_client.modules.ExecuteManager
import net.dimterex.sync_client.modules.SettingsManager
import net.dimterex.sync_client.presenter.base.BasePresenter
import net.dimterex.sync_client.presenter.base.BaseView
import org.kodein.di.erased.instance

class MainPresenter(private val view: MainView) : BasePresenter(view)
{
    private val _settings by instance<SettingsManager>()

    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)
        _settings.initialize()
    }
}

interface MainView : BaseView {}