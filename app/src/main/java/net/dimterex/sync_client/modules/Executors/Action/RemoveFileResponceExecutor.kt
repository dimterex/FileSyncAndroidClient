package net.dimterex.sync_client.modules.Executors.Action

import net.dimterex.sync_client.api.Message.Action.RemoveFileResponce
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.modules.EventLoggerManager
import net.dimterex.sync_client.modules.FileManager

class RemoveFileResponceExecutor(private val fileManager: FileManager, private val _eventLoggerManager: EventLoggerManager) : IExecute<RemoveFileResponce> {
    override fun Execute(param: RemoveFileResponce) {
        var filePath = fileManager.getFullPath(param.file_path, true)
        _eventLoggerManager.save_event(filePath?.absolutePath + param.file_name + "was removed ")
    }
}