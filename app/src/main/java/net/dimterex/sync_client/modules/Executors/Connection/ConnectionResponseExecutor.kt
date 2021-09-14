package net.dimterex.sync_client.modules.Executors.Connection

import net.dimterex.sync_client.api.Message.Connection.ConnectionResponse
import net.dimterex.sync_client.api.Modules.Common.IExecute

class ConnectionResponseExecutor : IExecute<ConnectionResponse> {
    override fun Execute(param: ConnectionResponse) {
        println(param.shared_folders)
    }
}