package net.dimterex.sync_client.modules.Executors.Info

import net.dimterex.sync_client.api.Message.Info.FileListRequest
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.modules.EventLoggerManager


class FineListRequestExecutor(private val _eventLoggerManager: EventLoggerManager) : IExecute<FileListRequest> {
    override fun Execute(param: FileListRequest) {
        println(param)
//        _eventLoggerManager.save_event(param.toString())
    }
}

