package net.dimterex.sync_client.modules.Executors.Action

import net.dimterex.sync_client.api.Message.Action.RemoveFileResponce
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.entity.FileSyncState
import net.dimterex.sync_client.modules.FileStateEventManager
import net.dimterex.sync_client.modules.FileManager

class RemoveFileResponseExecutor(private val fileManager: FileManager, private val _FileState_eventManager: FileStateEventManager) : IExecute<RemoveFileResponce> {
    override fun Execute(param: RemoveFileResponce) {
        var filePath = fileManager.getFullPath(param.file_name, true)
        _FileState_eventManager.save_event(FileSyncState(param.file_name, 100))
    }
}