package net.dimterex.sync_client.presenter.menu.sync

import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.dimterex.sync_client.api.Message.Sync.SyncStartFilesRequest
import net.dimterex.sync_client.data.ScopeFactory
import net.dimterex.sync_client.modules.ExecuteManager
import net.dimterex.sync_client.modules.SyncStateManager
import net.dimterex.sync_client.presenter.base.BasePresenter
import net.dimterex.sync_client.presenter.base.BaseView
import org.kodein.di.erased.instance


class SyncDialogPresenter(private val view: SyncDialogView) : BasePresenter(view) {

    private val _syncStateManager by instance<SyncStateManager>()
    private val _scopeFactory by instance<ScopeFactory>()
    private val _executerManager by instance<ExecuteManager>()

    private var _mainScope: CoroutineScope? = null

    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)
        _syncStateManager.subscribe_update(this::update)
        _mainScope = _scopeFactory.getMainScope()
    }

    override fun onDestroy() {
        super.onDestroy()
        _syncStateManager.unsubscribe_update()
    }

    private fun update(addedFiles: List<String>,
                       removedFiles: List<String>,
                       uploadedFiles: List<String>,
                       updatedFiles: List<String>,
                       serverRemoved: List<String>,
                       databaseAdded: List<String>) {
        _mainScope?.launch {
            view.update_added_files(addedFiles)
            view.update_removed_files(removedFiles)
            view.update_uploaded_files(uploadedFiles)
            view.update_update_files(updatedFiles)
            view.update_server_removed_files(serverRemoved)
            view.update_database_added_files(databaseAdded)
            view.enable_view()
        }
    }

    fun apply() {
        _executerManager.execute(SyncStartFilesRequest())
    }
}

interface SyncDialogView : BaseView {
    fun update_added_files(addedFiles: List<String>)
    fun update_removed_files(removedFiles: List<String>)
    fun update_uploaded_files(uploadedFiles: List<String>)
    fun update_update_files(updatedFiles: List<String>)
    fun update_server_removed_files(serverRemoved: List<String>)
    fun update_database_added_files(databaseAdded: List<String>)
    fun enable_view()
}