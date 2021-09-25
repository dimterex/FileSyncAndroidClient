package net.dimterex.sync_client.modules.Executors.Action

import net.dimterex.sync_client.api.Message.Action.SyncFilesRequest
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.modules.FileStateEventManager
import net.dimterex.sync_client.modules.ExecuteManager
import net.dimterex.sync_client.modules.FileManager

class SyncFilesRequestExecutor(
    private val _fileManager: FileManager,
    private val _executor: ExecuteManager
) : IExecute<SyncFilesRequest> {
    override fun Execute(param: SyncFilesRequest) {
        param.files = _fileManager.getFileList()
        _executor.sendMessage(param)
    }
}