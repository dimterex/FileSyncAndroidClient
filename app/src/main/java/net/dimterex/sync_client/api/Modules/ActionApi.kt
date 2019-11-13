package net.dimterex.sync_client.api.Modules

import net.dimterex.sync_client.modules.Executors.Action.AddFileResponceExecutor
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.api.interfaces.IMessage
import net.dimterex.sync_client.api.Message.Action.AddFileResponce
import net.dimterex.sync_client.api.Message.Action.RemoveFileResponce
import net.dimterex.sync_client.api.Modules.Common.BaseApiModule
import net.dimterex.sync_client.modules.EventLoggerManager
import net.dimterex.sync_client.modules.ExecuteManager
import net.dimterex.sync_client.modules.Executors.Action.RemoveFileResponceExecutor
import net.dimterex.sync_client.modules.FileManager

class ActionApi(private val _fileManager: FileManager, _executeManager: ExecuteManager, private val _eventLoggerManager: EventLoggerManager)
    : BaseApiModule(_executeManager) {

    override fun RegisterHandlers() {
        executeManager.initApiMessage(AddFileResponce::class.java, AddFileResponceExecutor(_fileManager, _eventLoggerManager) as IExecute<IMessage>)
        executeManager.initApiMessage(RemoveFileResponce::class.java, RemoveFileResponceExecutor(_fileManager, _eventLoggerManager) as IExecute<IMessage>)
    }
}