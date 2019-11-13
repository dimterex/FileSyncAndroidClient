package net.dimterex.sync_client.api.Modules

import net.dimterex.sync_client.api.Message.Info.FileListRequest
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.api.interfaces.IMessage
import net.dimterex.sync_client.api.Message.Action.SyncFilesRequest
import net.dimterex.sync_client.api.Message.Info.FileListResponce
import net.dimterex.sync_client.api.Modules.Common.BaseApiModule
import net.dimterex.sync_client.modules.EventLoggerManager
import net.dimterex.sync_client.modules.ExecuteManager
import net.dimterex.sync_client.modules.Executors.Action.SyncFilesRequestExecutor
import net.dimterex.sync_client.modules.Executors.Info.FileListResponceExecutor
import net.dimterex.sync_client.modules.Executors.Info.FineListRequestExecutor
import net.dimterex.sync_client.modules.FileManager

class InfoApi(private val _fileManager: FileManager, _executeManager: ExecuteManager, private val _eventLoggerManager: EventLoggerManager)
    : BaseApiModule(_executeManager) {

    override fun RegisterHandlers() {
        executeManager.initApiMessage(FileListRequest::class.java, FineListRequestExecutor(_eventLoggerManager) as IExecute<IMessage>)
        executeManager.initApiMessage(FileListResponce::class.java, FileListResponceExecutor(_fileManager, _eventLoggerManager) as IExecute<IMessage>)
        executeManager.initApiMessage(SyncFilesRequest::class.java, SyncFilesRequestExecutor(_fileManager, executeManager, _eventLoggerManager) as IExecute<IMessage>)
    }
}