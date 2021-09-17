package net.dimterex.sync_client.modules.Executors.Info

import net.dimterex.sync_client.api.Message.Info.FileListRequest
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.modules.FileStateEventManager


class FineListRequestExecutor(private val _FileState_eventManager: FileStateEventManager) : IExecute<FileListRequest> {
    override fun Execute(param: FileListRequest) {
        println(param)
//        _eventLoggerManager.save_event(param.toString())
    }
}

