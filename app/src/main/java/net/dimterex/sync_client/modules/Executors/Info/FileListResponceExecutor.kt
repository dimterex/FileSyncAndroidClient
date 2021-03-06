package net.dimterex.sync_client.modules.Executors.Info

import net.dimterex.sync_client.api.Message.Info.FileListResponce
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.modules.EventLoggerManager
import net.dimterex.sync_client.modules.FileManager

class FileListResponceExecutor(private val fileManager: FileManager, private val _eventLoggerManager: EventLoggerManager) : IExecute<FileListResponce> {

    override fun Execute(param: FileListResponce) {
        for (file in param.files )
            _eventLoggerManager.save_event(file.toString())
    }
}