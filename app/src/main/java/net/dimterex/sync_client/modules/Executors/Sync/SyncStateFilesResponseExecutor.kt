package net.dimterex.sync_client.modules.Executors.Sync

import net.dimterex.sync_client.api.Message.Sync.SyncStateFilesResponse
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.modules.*

class SyncStateFilesResponseExecutor(private val _syncStateManager: SyncStateManager)
    : IExecute<SyncStateFilesResponse> {

    private val TAG = this::class.java.name

    override fun Execute(param: SyncStateFilesResponse) {
        _syncStateManager.syncStateFilesResponse = param
    }
}