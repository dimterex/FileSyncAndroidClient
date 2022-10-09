package net.dimterex.sync_client.modules.Executors.Sync

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.dimterex.sync_client.api.Message.Sync.SyncStateFilesRequest
import net.dimterex.sync_client.api.Message.Sync.SyncStateFilesResponse
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.data.ScopeFactory
import net.dimterex.sync_client.modules.*

class SyncStateFilesRequestExecutor(
    private val _fileManager: FileManager,
    private val _jsonManager: JsonManager,
    private val _fileState_eventManager: FileStateEventManager,
    _scopeFactory: ScopeFactory
) : IExecute<SyncStateFilesRequest> {

    private val TAG = this::class.java.name

    private val scope: CoroutineScope = _scopeFactory.getScope()

    override fun Execute(param: SyncStateFilesRequest) {
        param.folders = _fileManager.getFolderInfos()
        _fileState_eventManager.clear_log()
        startProcessing(param)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    fun startProcessing(param: SyncStateFilesRequest) {

        scope.launch {
            try {
                val response = _jsonManager.getPostMessage(param)
                if (!response.isSuccessful)
                    throw Exception("File not found!")

                val inputStream = response.body()?.string()

                _jsonManager.restResponse(inputStream?:String(), SyncStateFilesResponse::class.java)
            } catch (e: Throwable) {
                println(e)
                e.printStackTrace()
            }
        }
    }
}