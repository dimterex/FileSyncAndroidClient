package net.dimterex.sync_client.modules.Executors.Connection

import net.dimterex.sync_client.api.Message.Connection.ConnectionResponse
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.modules.AvailableFoldersManager
import net.dimterex.sync_client.modules.ConnectionManager
import net.dimterex.sync_client.modules.FileManager

class ConnectionResponseExecutor(private val _availableFoldersManager: AvailableFoldersManager,
                                 private val _fileManager: FileManager,
                                 private val _connectionManager: ConnectionManager) : IExecute<ConnectionResponse> {
    override fun Execute(param: ConnectionResponse) {
        _connectionManager.setToken(param.token)
        param.shared_folders.forEach{ x ->
            _availableFoldersManager.save_event(_fileManager.joinToString(x.path))
        }

        _connectionManager.raiseConnection()
    }
}