package net.dimterex.sync_client.api.Modules

import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.api.interfaces.IMessage
import net.dimterex.sync_client.api.Message.Sync.SyncFilesRequest
import net.dimterex.sync_client.api.Message.Sync.SyncFilesResponse
import net.dimterex.sync_client.modules.ExecuteManager
import net.dimterex.sync_client.modules.Executors.Sync.SyncFilesRequestExecutor
import net.dimterex.sync_client.modules.Executors.Sync.SyncFilesResponseExecutor

class SyncApi(_executeManager: ExecuteManager,
              _syncFilesRequestExecutor: SyncFilesRequestExecutor,
              _syncFilesResponseExecutor: SyncFilesResponseExecutor
) {

    init {
        _executeManager.initApiMessage(SyncFilesRequest::class.java, _syncFilesRequestExecutor as IExecute<IMessage>)
        _executeManager.initApiMessage(SyncFilesResponse::class.java, _syncFilesResponseExecutor as IExecute<IMessage>)
    }
}