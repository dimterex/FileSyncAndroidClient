package net.dimterex.sync_client.modules.Executors.Action

import android.net.Uri
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import net.dimterex.sync_client.api.Message.Action.AddFileResponce
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.data.FileInfo
import net.dimterex.sync_client.data.ScopeFactory
import net.dimterex.sync_client.entity.FileSyncState
import net.dimterex.sync_client.modules.ConnectionManager
import net.dimterex.sync_client.modules.FileStateEventManager
import net.dimterex.sync_client.modules.FileManager
import java.io.InputStream
import java.io.OutputStream

class AddFileResponseExecutor(val fileManager: FileManager,
                              private val _FileState_eventManager: FileStateEventManager,
                              private val _connectionManager: ConnectionManager,
                              private val _scopeFactory: ScopeFactory
) : IExecute<AddFileResponce> {


    private val TAG = this::class.java.name

    private val downloadQueue = Channel<FileInfo>()
    private var mainJob: Job? = null

    private val scope: CoroutineScope = _scopeFactory.getScope()

    init {
        startProcessing()
    }

    override fun Execute(param: AddFileResponce) {

        scope.launch {

            val fileInfo = FileInfo(param.file_name, sizeBytes = param.size)
            val fileSyncState = FileSyncState(param.file_name)
            _FileState_eventManager.save_event(fileSyncState)
             downloadQueue.send(fileInfo)
        }
    }


    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun writeResponseStreamToDisk(fileInfo: FileInfo, uri: Uri, outputStream: OutputStream, inputStream: InputStream) {
        var fileSizeDownloaded = 0L

        try {
            val fileReader = ByteArray(10240)
            var lastProgress = -1


            while (true) {
                val read = inputStream.read(fileReader)
                if (read == -1) {
                    break
                }
                outputStream.write(fileReader, 0, read)
                fileSizeDownloaded += read.toLong()

                val progress = (fileSizeDownloaded.toFloat() / fileInfo.sizeBytes * 100).toInt()

                //ignore spam
                if (lastProgress != progress) {
                    lastProgress = progress
                    withContext(Dispatchers.Main) {
                        val fileSyncState = FileSyncState(fileInfo.name, progress)
                        _FileState_eventManager.save_event(fileSyncState)
                    }
                }
            }
            outputStream.flush()

        } catch (ex: Exception) {
            Log.e("Error", "${ex.message}\n${ex.stackTrace.joinToString("\n")}\n${ex.cause?.stackTrace?.joinToString("\n")}")
        } finally {
            inputStream.close()
            outputStream.close()
        }
    }


    @Suppress("BlockingMethodInNonBlockingContext")
    fun startProcessing() {
        mainJob?.cancel()

        mainJob = scope.launch {
            /**Download**/
            launch(Dispatchers.IO) {
                Log.d(TAG, "download coroutine launched")
                while(true) {
                    val item = downloadQueue.receive()
                    val job = launch {

                        try {
                            val response = _connectionManager.download(item.name)

                            if (!response.isSuccessful)
                                throw Exception("Attachment not found!")

                            val inputStream = response.body()?.byteStream()

                            inputStream?.let { stream ->
                                val streamAndUri = fileManager.getFileOutputStreamAndURI(item.name)
                                if (streamAndUri.first != null) {
                                    writeResponseStreamToDisk(item, streamAndUri.second, streamAndUri.first!!, stream)
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