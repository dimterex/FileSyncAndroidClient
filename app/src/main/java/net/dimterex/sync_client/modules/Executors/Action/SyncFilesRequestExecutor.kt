package net.dimterex.sync_client.modules.Executors.Action

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.dimterex.sync_client.api.Message.Action.SyncFilesRequest
import net.dimterex.sync_client.api.Message.Action.SyncFilesResponse
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.api.interfaces.IMessage
import net.dimterex.sync_client.data.ScopeFactory
import net.dimterex.sync_client.entity.FileSyncState
import net.dimterex.sync_client.modules.*

class SyncFilesRequestExecutor(
    private val _fileManager: FileManager,
    private val _jsonManager: JsonManager,
    private val _connectionManager: ConnectionManager,
    private val _scopeFactory: ScopeFactory
) : IExecute<SyncFilesRequest> {

    private val TAG = this::class.java.name

    private val scope: CoroutineScope = _scopeFactory.getScope()

    override fun Execute(param: SyncFilesRequest) {
        param.files = _fileManager.getFileList()
        startProcessing(param)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    fun startProcessing(param: SyncFilesRequest) {

        scope.launch {
            try {
                val response = _connectionManager.sync(param.files.toTypedArray())
                println(response)
                if (!response.isSuccessful)
                    throw Exception("Attachment not found!")

                val inputStream = response.body()?.string()

                _jsonManager.restResponse(inputStream?:String(), SyncFilesResponse::class.java)
            } catch (e: Throwable) {
                println(e)
                e.printStackTrace()
            }
        }
    }
}