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

    private fun update(addedFilesCount: Int,
                       removedFilesCount: Int,
                       uploadedFilesCount: Int,
                       updatedFilesCount: Int,
                       serverRemovedCount: Int,
                       databaseAddedCount: Int) {
        _mainScope?.launch {
            view.update_added_files(addedFilesCount)
            view.update_removed_files(removedFilesCount)
            view.update_uploaded_files(uploadedFilesCount)
            view.update_update_files(updatedFilesCount)
            view.update_server_removed_files(serverRemovedCount)
            view.update_database_added_files(databaseAddedCount)
        }
    }

    fun apply() {
        _executerManager.execute(SyncStartFilesRequest())
    }

}

interface SyncDialogView : BaseView {
    fun update_added_files(addedFilesCount: Int)
    fun update_removed_files(removedFilesCount: Int)
    fun update_uploaded_files(uploadedFilesCount: Int)
    fun update_update_files(updatedFilesCount: Int)
    fun update_server_removed_files(serverRemovedCount: Int)
    fun update_database_added_files(databaseAddedCount: Int)
}