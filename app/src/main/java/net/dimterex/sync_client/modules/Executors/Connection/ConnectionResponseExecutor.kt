package net.dimterex.sync_client.modules.Executors.Connection

import net.dimterex.sync_client.api.Message.Connection.ConnectionResponse
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.entity.FileSyncState
import net.dimterex.sync_client.modules.AvailableFoldersManager
import net.dimterex.sync_client.modules.ConnectionManager
import net.dimterex.sync_client.modules.FileStateEventManager

class ConnectionResponseExecutor(private val _availableFoldersManager: AvailableFoldersManager,
private val _connectionManager: ConnectionManager) : IExecute<ConnectionResponse> {
    override fun Execute(param: ConnectionResponse) {
        _connectionManager.setToken(param.token)
        param.shared_folders.forEach{ x ->
            _availableFoldersManager.save_event(x)
        }
    }
}