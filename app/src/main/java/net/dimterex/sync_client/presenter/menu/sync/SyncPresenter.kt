package net.dimterex.sync_client.presenter.menu.sync

import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.dimterex.sync_client.api.Message.Sync.SyncStateFilesRequest
import net.dimterex.sync_client.data.ScopeFactory
import net.dimterex.sync_client.entity.FileSyncState
import net.dimterex.sync_client.modules.*
import net.dimterex.sync_client.presenter.base.BasePresenter
import net.dimterex.sync_client.presenter.base.BaseView
import org.kodein.di.erased.instance

class SyncPresenter(private val view: SyncView) : BasePresenter(view) {

    private val _executerManager by instance<ExecuteManager>()
    private val _event_log_manager by instance<FileStateEventManager>()
    private val _connectionManager by instance<ConnectionManager>()
    private val _scopeFactory by instance<ScopeFactory>()
    private val _syncStateEventManager by instance<SyncStateEventManager>()
    private val _syncStateManager by instance<SyncStateManager>()

    private var _mainScope: CoroutineScope? = null

    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)

        view.update(_event_log_manager.logs)
        _event_log_manager.subscribe_added_event(this::add_event_listener)
        _event_log_manager.subscribe_updated_event(this::update_item)
        _event_log_manager.subscribe_clear_event(this::clear_event)
        _connectionManager.subscribe_connection_state_change_event(this::connectedStateChange)
        _syncStateEventManager.subscribe_update_event(this::updateSyncState)

        _mainScope = _scopeFactory.getMainScope()
        connectedStateChange(_connectionManager.isConnected)
    }

    override fun onDestroy() {
        super.onDestroy()
        _event_log_manager.unsubscribe_added_event()
        _event_log_manager.unsubscribe_updated_event()
        _event_log_manager.unsubscribe_clear_event()
        _connectionManager.unsubscribe_connection_state_change_event(this::connectedStateChange)
        _syncStateEventManager.unsubscribe_update_event()
    }

    fun connectedStateChange(isConnected: Boolean) {
        _mainScope?.launch {
            view.update_connected(isConnected)
        }
    }

    fun can_sync_execute(): Boolean {
        if (_connectionManager.isConnected)
        {
            _executerManager.execute(SyncStateFilesRequest())
            return true
        }
        _connectionManager.restart_connection()
        return false
    }

    fun executeLast() {
        _syncStateManager.changeState(false)
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

    private fun updateSyncState(state: String) {
        view.updateSyncState(state)
    }

    private fun clear_event(){
        view.clear_events()
    }
}

interface SyncView : BaseView {

    fun add_new_event(message: FileSyncState)
    fun clear_events()
    fun update(logs: ArrayList<FileSyncState>)
    fun update_position(position: Int)
    fun update_connected(isConnected: Boolean)
    fun updateSyncState(state: String)
}