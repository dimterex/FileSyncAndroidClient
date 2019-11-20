package net.dimterex.sync_client.modules.Executors.Action

import net.dimterex.sync_client.api.Message.Action.SyncFilesRequest
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.modules.EventLoggerManager
import net.dimterex.sync_client.modules.ExecuteManager
import net.dimterex.sync_client.modules.FileManager

class SyncFilesRequestExecutor(
    private val _fileManager: FileManager,
    private val _executor: ExecuteManager,
    private val _eventLoggerManager: EventLoggerManager
) : IExecute<SyncFilesRequest> {
    override fun Execute(param: SyncFilesRequest) {
        param.files = _fileManager.getFileList()
//        _eventLoggerManager.save_event(param.toString())
        _executor.sendMessage(param)
    }
}