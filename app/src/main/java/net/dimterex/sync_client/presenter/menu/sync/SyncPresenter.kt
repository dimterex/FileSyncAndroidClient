package net.dimterex.sync_client.presenter.menu.sync

import android.os.Bundle
import net.dimterex.sync_client.api.Message.Action.SyncFilesRequest
import net.dimterex.sync_client.entity.EventDto
import net.dimterex.sync_client.modules.EventLoggerManager
import net.dimterex.sync_client.modules.ExecuteManager
import net.dimterex.sync_client.presenter.base.BasePresenter
import net.dimterex.sync_client.presenter.base.BaseView
import org.kodein.di.erased.instance

class SyncPresenter(private val view: SyncView) : BasePresenter(view) {

    private val _executerManager by instance<ExecuteManager>()
    private val _event_log_manager by instance<EventLoggerManager>()

    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)
        _event_log_manager.add_event_listener(this::add_event_listener)
    }

    fun sync_execute() {
        _executerManager.execute(SyncFilesRequest())
    }

    fun onRepoPressed(id: Long) {
        view.navigateRepoDetails(id)
    }

    fun add_event_listener(string: String) {
        view.event_change(string)
    }

}

interface SyncView : BaseView {
    var repos: List<EventDto>

    fun navigateRepoDetails(id: Long)

    fun event_change(string: String)
}