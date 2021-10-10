package net.dimterex.sync_client.modules.Executors.Action

import android.net.Uri
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import net.dimterex.sync_client.api.Message.Action.AddFileResponce
import net.dimterex.sync_client.api.Message.Action.FileUploadRequest
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.data.FileInfo
import net.dimterex.sync_client.data.ScopeFactory
import net.dimterex.sync_client.entity.FileSyncState
import net.dimterex.sync_client.entity.FileSyncType
import net.dimterex.sync_client.modules.ConnectionManager
import net.dimterex.sync_client.modules.FileStateEventManager
import net.dimterex.sync_client.modules.FileManager
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.*

class FileUploadRequestExecutor(val fileManager: FileManager,
                                private val _FileState_eventManager: FileStateEventManager,
                                private val _connectionManager: ConnectionManager,
                                private val _scopeFactory: ScopeFactory
) : IExecute<FileUploadRequest> {


    private val TAG = this::class.java.name

    private val uploadQueue = Channel<Pair<FileInfo, String>>()
    private var mainJob: Job? = null

    private val scope: CoroutineScope = _scopeFactory.getScope()

    init {
        startProcessing()
    }

    override fun Execute(param: FileUploadRequest) {

        scope.launch {
            val fileInfo = fileManager.getFileInfoForUpload(param.file_name)
            val fileSyncState = FileSyncState(fileInfo.second, FileSyncType.UPLOAD)

            _FileState_eventManager.save_event(fileSyncState)
            uploadQueue.send(fileInfo)
        }
    }


    @Suppress("BlockingMethodInNonBlockingContext")
    fun startProcessing() {
        mainJob?.cancel()

        mainJob = scope.launch {
            /**Upload**/
            launch(Dispatchers.IO) {
                Log.d(TAG, "upload coroutine launched")
                while(true){
                    val item = uploadQueue.receive()
                    val job = launch upload@{

                        Log.d(TAG, "uploadQueue item: $item")

//                        val fileInfo = fileInfoStorage.getFileInfo(item.messageId, item.name) ?: return@upload
//                        item.uri?:return@upload

//                        withContext(Dispatchers.Main) {
//                            fileInfoStorage.update(item, PROGRESS(0), null, currentJob)
//                        }

                        try {
                            val mediaType = MediaType.parse("application/octet-stream")


                            val inputStream = File(item.first.name).inputStream()

                            val requestBody = inputStream.asRequestBodyWithProgress(
                                contentType = mediaType,
                                contentLength = item.first.sizeBytes,
                                progressCallback = { progress ->

//                                    launch(Dispatchers.Main) {
//                                        fileInfoStorage.update(item, PROGRESS(progress), item.uri)
                                        _FileState_eventManager.save_event(FileSyncState(item.second, FileSyncType.UPLOAD, progress))
//                                    }
                                },
                                errorCallback = { e ->
//                                    launch(Dispatchers.Main) {
//                                        fileInfoStorage.update(item, FAIL(e))
//                                    }
                                    println(e)
                                    coroutineContext.cancelChildren()
//                                    job.cancel("error", e)
                                }
                            )

                            requestBody.let { body ->
                                val response = _connectionManager.upload(item.second, body)

                                withContext(Dispatchers.Main) {
                                    if (response.isSuccessful) {
//                                        fileInfoStorage.update(item, READY_UPLOAD(), item.uri)
                                    } else {
                                        val error = response.errorBody()?.charStream()?.readText()
//                                        fileInfoStorage.update(item, FAIL(Throwable(error)), item.uri)
                                    }
                                }
                            }

                        } catch (e: Throwable) {
//                            withContext(Dispatchers.Main) {
//                                fileInfoStorage.update(item, FAIL(e))
//                            }
                            e.printStackTrace()
                        }
                    }
                    job.join()
                }
            }
        }
    }

    /** Returns a new request body that transmits the content of this. */
    suspend fun InputStream.asRequestBodyWithProgress(
        contentType: MediaType? = null,
        contentLength: Long,
        progressCallback: ((progress: Int) -> Unit)?,
        errorCallback: ((e: Throwable) -> Unit)?
    ): RequestBody {
        return object : RequestBody() {
            override fun contentType() = contentType

            override fun contentLength(): Long = try {
                contentLength
                //available().toLong()
            } catch (e: IOException) {
                e.printStackTrace()
                0
            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                Log.d(TAG, "writeTo called")
                val fileLength = contentLength()
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                val inputStream = this@asRequestBodyWithProgress
                var uploaded = 0L
                var lastProgress = -1
                try {
                    var read: Int = inputStream.read(buffer)
                    while (read != -1) {
                        uploaded += read
                        val progress = (uploaded / fileLength.toFloat() * 100).toInt()

                        //ignore spam
                        if(lastProgress != progress) {
                            lastProgress = progress
                            progressCallback?.invoke(progress)
                        }

                        sink.write(buffer, 0, read)

                        read = inputStream.read(buffer)
                    }
                } catch (e: Throwable) {
                    errorCallback?.invoke(e)
                    e.printStackTrace()
                } finally {
                    try {
                        if (sink.isOpen) {
                            sink.flush()
                            sink.close()
                        }
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }

                    inputStream.close()
                }
            }
        }
    }
}