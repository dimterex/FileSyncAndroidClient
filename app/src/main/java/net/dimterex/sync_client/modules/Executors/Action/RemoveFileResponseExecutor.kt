package net.dimterex.sync_client.modules.Executors.Action

import android.annotation.SuppressLint
import android.content.AsyncQueryHandler
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import net.dimterex.sync_client.api.Message.Action.RemoveFileResponce
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.data.ScopeFactory
import net.dimterex.sync_client.entity.FileSyncState
import net.dimterex.sync_client.entity.FileSyncType
import net.dimterex.sync_client.modules.FileManager
import net.dimterex.sync_client.modules.FileStateEventManager
import java.io.*


class RemoveFileResponseExecutor(private val fileManager: FileManager,
                                 private val _FileState_eventManager: FileStateEventManager,
                                 private val _scopeFactory: ScopeFactory,
                                 private val _appContext: Context
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

            val fileSyncState = FileSyncState(file.path, FileSyncType.DELETE)
            _FileState_eventManager.save_event(fileSyncState)
            removeQueue.send(file)
        }
    }

    @SuppressLint("NewApi")
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
//                            java.nio.file.Files.delete(item.toPath())

////                            val test =  FileInputStream(item)
//                            val uri = Uri.fromFile(item)
//
//                            println(uri)
//                            val baseDir = Environment.getExternalStorageDirectory().absolutePath;
//                            println(baseDir)
//                            val f = File(baseDir + "/0390-0807/English_File_4th_edition_Pre_Intermediate_Workbook.pdf");
//                            println(f)
//                            val deleted = ;


                            if (item.exists()) {
                                if (item.delete()) {
                                    println("file Deleted $item")
                                    _FileState_eventManager.save_event(FileSyncState(item.path, FileSyncType.DELETE, 100))
                                } else {
                                    println("file not Deleted $item");
                                }
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