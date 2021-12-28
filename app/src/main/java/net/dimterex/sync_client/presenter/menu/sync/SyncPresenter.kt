package net.dimterex.sync_client.presenter.menu.sync

import android.os.Bundle
import net.dimterex.sync_client.api.Message.Sync.SyncFilesRequest
import net.dimterex.sync_client.entity.FileSyncState
import net.dimterex.sync_client.modules.ConnectionManager
import net.dimterex.sync_client.modules.FileStateEventManager
import net.dimterex.sync_client.modules.ExecuteManager
import net.dimterex.sync_client.presenter.base.BasePresenter
import net.dimterex.sync_client.presenter.base.BaseView
import org.kodein.di.erased.instance

class SyncPresenter(private val view: SyncView) : BasePresenter(view) {

    private val _executerManager by instance<ExecuteManager>()
    private val _event_log_manager by instance<FileStateEventManager>()
    private val _connectionManager by instance<ConnectionManager>()

    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)

        view.update(_event_log_manager.logs)
        _event_log_manager.add_event_listener(this::add_event_listener, this::update_item)
        _connectionManager.addConnectionStateListener(this::connectedStateChange)
        connectedStateChange(_connectionManager.isConnected)
    }


    fun connectedStateChange(isConnected: Boolean) {
        view.update_connected(isConnected)
    }

    fun sync_execute() {
        _executerManager.execute(SyncFilesRequest())
    }

    fun onRepoPressed(id: String) {
        println(id)
//        view.navigateRepoDetails(id)
    }

    private fun add_event_listener(message: FileSyncState){
        view.add_new_event(message)
    }

    private fun update_item(position: Int) {
        view.update_position(position)
    }
}

interface SyncView : BaseView {

    fun add_new_event(message: FileSyncState)
    fun update(logs: ArrayList<FileSyncState>)
    fun update_position(position: Int)

    fun update_connected(isConnected: Boolean)
}