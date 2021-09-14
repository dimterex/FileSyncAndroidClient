package net.dimterex.sync_client.api.Modules

import net.dimterex.sync_client.api.Message.Connection.ConnectionRequest
import net.dimterex.sync_client.api.Message.Connection.ConnectionResponse
import net.dimterex.sync_client.api.Modules.Common.BaseApiModule
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.api.interfaces.IMessage
import net.dimterex.sync_client.modules.ExecuteManager
import net.dimterex.sync_client.modules.Executors.Connection.ConnectionRequestExecutor
import net.dimterex.sync_client.modules.Executors.Connection.ConnectionResponseExecutor


class ConnectionApi(_executeManager: ExecuteManager)
    : BaseApiModule(_executeManager) {

    override fun RegisterHandlers() {
        executeManager.initApiMessage(ConnectionRequest::class.java, ConnectionRequestExecutor() as IExecute<IMessage>)
        executeManager.initApiMessage(ConnectionResponse::class.java, ConnectionResponseExecutor() as IExecute<IMessage>)
    }
}