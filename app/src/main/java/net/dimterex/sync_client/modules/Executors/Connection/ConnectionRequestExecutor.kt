package net.dimterex.sync_client.modules.Executors.Connection

import net.dimterex.sync_client.api.Message.Connection.ConnectionRequest
import net.dimterex.sync_client.api.Message.Connection.ConnectionResponse
import net.dimterex.sync_client.api.Modules.Common.IExecute

class ConnectionRequestExecutor : IExecute<ConnectionRequest> {
    override fun Execute(param: ConnectionRequest) {
        println(param)
    }
}