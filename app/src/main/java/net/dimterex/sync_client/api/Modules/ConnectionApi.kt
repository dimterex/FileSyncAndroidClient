package net.dimterex.sync_client.api.Modules

import net.dimterex.sync_client.api.Message.Connection.ConnectionRequest
import net.dimterex.sync_client.api.Message.Connection.ConnectionResponse
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.api.interfaces.IMessage
import net.dimterex.sync_client.modules.ExecuteManager
import net.dimterex.sync_client.modules.Executors.Connection.ConnectionRequestExecutor
import net.dimterex.sync_client.modules.Executors.Connection.ConnectionResponseExecutor


class ConnectionApi(_executeManager: ExecuteManager,
                    _connectionRequestExecutor: ConnectionRequestExecutor,
                    _connectionResponseExecutor: ConnectionResponseExecutor)
{

    init {
        _executeManager.initApiMessage(ConnectionRequest::class.java, _connectionRequestExecutor as IExecute<IMessage>)
        _executeManager.initApiMessage(ConnectionResponse::class.java, _connectionResponseExecutor as IExecute<IMessage>)
    }
}