package net.dimterex.sync_client.api.Modules

import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.api.interfaces.IMessage
import net.dimterex.sync_client.api.Message.Action.SyncFilesRequest
import net.dimterex.sync_client.modules.ExecuteManager
import net.dimterex.sync_client.modules.Executors.Action.SyncFilesRequestExecutor

class InfoApi(_executeManager: ExecuteManager,
              _syncFilesRequestExecutor: SyncFilesRequestExecutor) {

    init {
        _executeManager.initApiMessage(SyncFilesRequest::class.java, _syncFilesRequestExecutor as IExecute<IMessage>)
    }
}