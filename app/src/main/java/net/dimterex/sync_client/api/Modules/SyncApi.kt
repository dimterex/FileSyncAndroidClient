package net.dimterex.sync_client.api.Modules

import net.dimterex.sync_client.api.Message.Sync.SyncStartFilesRequest
import net.dimterex.sync_client.api.Message.Sync.SyncStartFilesResponse
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.api.interfaces.IMessage
import net.dimterex.sync_client.api.Message.Sync.SyncStateFilesRequest
import net.dimterex.sync_client.api.Message.Sync.SyncStateFilesResponse
import net.dimterex.sync_client.modules.ExecuteManager
import net.dimterex.sync_client.modules.Executors.Sync.SyncStartFilesRequestExecutor
import net.dimterex.sync_client.modules.Executors.Sync.SyncStartFilesResponseExecutor
import net.dimterex.sync_client.modules.Executors.Sync.SyncStateFilesRequestExecutor
import net.dimterex.sync_client.modules.Executors.Sync.SyncStateFilesResponseExecutor

class SyncApi(_executeManager: ExecuteManager,
              _syncFilesRequestExecutor: SyncStateFilesRequestExecutor,
              _syncFilesResponseExecutor: SyncStateFilesResponseExecutor,
              _syncStartFilesRequestExecutor: SyncStartFilesRequestExecutor,
              _syncStartFilesResponseExecutor: SyncStartFilesResponseExecutor
) {

    init {
        _executeManager.initApiMessage(SyncStateFilesRequest::class.java, _syncFilesRequestExecutor as IExecute<IMessage>)
        _executeManager.initApiMessage(SyncStateFilesResponse::class.java, _syncFilesResponseExecutor as IExecute<IMessage>)
        _executeManager.initApiMessage(SyncStartFilesRequest::class.java, _syncStartFilesRequestExecutor as IExecute<IMessage>)
        _executeManager.initApiMessage(SyncStartFilesResponse::class.java, _syncStartFilesResponseExecutor as IExecute<IMessage>)
    }
}