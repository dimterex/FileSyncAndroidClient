package net.dimterex.sync_client.modules.Executors.Action

import android.net.Uri
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import net.dimterex.sync_client.api.Message.Action.AddFileResponce
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.data.FileInfo
import net.dimterex.sync_client.data.entries.ConnectionsLocalModel
import net.dimterex.sync_client.entity.EventDto
import net.dimterex.sync_client.modules.ConnectionManager
import net.dimterex.sync_client.modules.EventLoggerManager
import net.dimterex.sync_client.modules.Executors.Transport.IAttachmentRestApi
import net.dimterex.sync_client.modules.Executors.Transport.rest.RestClientBuilder
import net.dimterex.sync_client.modules.FileManager
import net.dimterex.sync_client.modules.SettingsManager
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class AddFileResponseExecutor(val fileManager: FileManager,
                              private val _eventLoggerManager: EventLoggerManager,
                              private val _connectionManager: ConnectionManager
) : IExecute<AddFileResponce> {


    private val TAG = this::class.java.name

    private val downloadQueue = Channel<FileInfo>()
    private var mainJob: Job? = null

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + Job())

    init {
        startProcessing()
    }

    override fun Execute(param: AddFileResponce) {

        scope.launch {

            val fileInfo = FileInfo(param.file_name, sizeBytes = param.size)
            val eventDto = EventDto(param.file_name, "In queue")
            _eventLoggerManager.save_event(eventDto)

            downloadQueue.send(fileInfo)
        }
    }


    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun writeResponseStreamToDisk(fileInfo: FileInfo, uri: Uri, outputStream: OutputStream, inputStream: InputStream) {
        var fileSizeDownloaded = 0L

        println("Size: " + fileInfo.sizeBytes)

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

//                println("Size Downloaded: " + fileSizeDownloaded)
                val progress = (fileSizeDownloaded.toFloat() / fileInfo.sizeBytes * 100).toInt()
//                println("Process: " + progress)

                //ignore spam
                if (lastProgress != progress) {
                    lastProgress = progress
                    withContext(Dispatchers.Main) {
                        val eventDto = EventDto(fileInfo.name, progress.toString())
//                        fileInfo.name + " was saved "+ progress + "%"
                        _eventLoggerManager.save_event(eventDto)
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
//                        val currentJob = coroutineContext.job
//                        withContext(Dispatchers.Main) {
//                            fileInfoStorage.update(item, PROGRESS(0), null, currentJob)
//                        }

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