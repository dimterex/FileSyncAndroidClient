package net.dimterex.sync_client.modules.Executors.Action

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import net.dimterex.sync_client.api.Message.Action.RemoveFileResponce
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.data.FileInfo
import net.dimterex.sync_client.data.ScopeFactory
import net.dimterex.sync_client.entity.FileSyncState
import net.dimterex.sync_client.modules.FileStateEventManager
import net.dimterex.sync_client.modules.FileManager
import java.io.File

class RemoveFileResponseExecutor(private val fileManager: FileManager,
                                 private val _FileState_eventManager: FileStateEventManager,
                                 private val _scopeFactory: ScopeFactory
) : IExecute<RemoveFileResponce> {


    private val TAG = this::class.java.name
    private val removeQueue = Channel<File>()
    private var mainJob: Job? = null

    private val scope: CoroutineScope = _scopeFactory.getScope()


    init {
        startProcessing()
    }

    override fun Execute(param: RemoveFileResponce) {

        scope.launch {

            val file = fileManager.getFullPath(param.file_name) ?: return@launch
            val fileSyncState = FileSyncState(file.path)
            _FileState_eventManager.save_event(fileSyncState)
            removeQueue.send(file)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    fun startProcessing() {
        mainJob?.cancel()

        mainJob = scope.launch {
            /**Remove**/
            launch(Dispatchers.IO) {
                Log.d(TAG, "download coroutine launched")
                while(true) {
                    val item = removeQueue.receive()
                    val job = launch {

                        try {
                            if (item.exists())
                                item.delete()

                            launch(Dispatchers.Main) {
                                _FileState_eventManager.save_event(FileSyncState(item.path, 100))
                            }

                        } catch (e: Throwable) {
                            println(e)
                            e.printStackTrace()
                        }
                    }
                    job.join()
                }
            }
        }
    }
}