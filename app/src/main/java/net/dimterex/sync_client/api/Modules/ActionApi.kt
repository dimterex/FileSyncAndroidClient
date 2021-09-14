package net.dimterex.sync_client.api.Modules

import net.dimterex.sync_client.modules.Executors.Action.AddFileResponseExecutor
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.api.interfaces.IMessage
import net.dimterex.sync_client.api.Message.Action.AddFileResponce
import net.dimterex.sync_client.api.Message.Action.RemoveFileResponce
import net.dimterex.sync_client.api.Modules.Common.BaseApiModule
import net.dimterex.sync_client.modules.*
import net.dimterex.sync_client.modules.Executors.Action.RemoveFileResponseExecutor
import net.dimterex.sync_client.modules.Executors.Transport.rest.RestClientBuilder

class ActionApi(private val _fileManager: FileManager,
                _executeManager: ExecuteManager,
                private val _eventLoggerManager: EventLoggerManager,
                private val _connectionManager: ConnectionManager)
    : BaseApiModule(_executeManager) {

    override fun RegisterHandlers() {
        executeManager.initApiMessage(AddFileResponce::class.java, AddFileResponseExecutor(_fileManager, _eventLoggerManager, _connectionManager) as IExecute<IMessage>)
        executeManager.initApiMessage(RemoveFileResponce::class.java, RemoveFileResponseExecutor(_fileManager, _eventLoggerManager) as IExecute<IMessage>)
    }
}