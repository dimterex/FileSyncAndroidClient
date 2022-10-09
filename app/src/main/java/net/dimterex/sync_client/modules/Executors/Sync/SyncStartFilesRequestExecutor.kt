package net.dimterex.sync_client.modules.Executors.Sync

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.dimterex.sync_client.api.Message.Sync.SyncStartFilesRequest
import net.dimterex.sync_client.api.Message.Sync.SyncStartFilesResponse
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.data.ScopeFactory
import net.dimterex.sync_client.modules.JsonManager

class SyncStartFilesRequestExecutor(private val _jsonManager: JsonManager,
                                    private val _scopeFactory: ScopeFactory
) : IExecute<SyncStartFilesRequest> {

    private val scope: CoroutineScope = _scopeFactory.getScope()

    override fun Execute(param: SyncStartFilesRequest) {
        startProcessing(param)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    fun startProcessing(param: SyncStartFilesRequest) {

        scope.launch {
            try {
                val response = _jsonManager.getPostMessage(param)
                if (!response.isSuccessful)
                    throw Exception("Attachment not found!")

                val inputStream = response.body()?.string()

                _jsonManager.restResponse(inputStream?:String(), SyncStartFilesResponse::class.java)
            } catch (e: Throwable) {
                println(e)
                e.printStackTrace()
            }
        }
    }
}