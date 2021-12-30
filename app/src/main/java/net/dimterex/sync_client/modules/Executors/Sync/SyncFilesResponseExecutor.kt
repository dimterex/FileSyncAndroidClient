package net.dimterex.sync_client.modules.Executors.Sync

import android.net.Uri
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.channels.Channel
import net.dimterex.sync_client.api.Message.Sync.SyncFilesResponse
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.data.FileInfo
import net.dimterex.sync_client.data.ScopeFactory
import net.dimterex.sync_client.entity.FileSyncState
import net.dimterex.sync_client.entity.FileSyncType
import net.dimterex.sync_client.modules.ConnectionManager
import net.dimterex.sync_client.modules.FileManager
import net.dimterex.sync_client.modules.FileStateEventManager
import net.dimterex.sync_client.modules.SyncStateEventManager
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class SyncFilesResponseExecutor(private val fileManager: FileManager,
                                private val _fileState_eventManager: FileStateEventManager,
                                private val _syncStateEventManager: SyncStateEventManager,
                                private val _connectionManager: ConnectionManager,
                                private val _scopeFactory: ScopeFactory)
    : IExecute<SyncFilesResponse> {

    private val TAG = this::class.java.name

    private val actionsQueue = Channel<FileInfo>()
    private val scope: CoroutineScope = _scopeFactory.getScope()
    private var eventsCount = 0
    private var finishedCount = 0

    override fun Execute(param: SyncFilesResponse) {

        eventsCount = param.added_files.count() +
                    param.removed_files.count() +
                    param.uploaded_files.count() +
                    param.updated_files.count()

            var corrent_count = 0

            for (for_remove in param.removed_files)
            {
                val path = fileManager.joinToString(for_remove.file_name)
                val file = fileManager.getFullPath(path) ?: continue

                corrent_count++
                val fileSyncState = FileSyncState(file.path, FileSyncType.DELETE, "$corrent_count/$eventsCount")
                _fileState_eventManager.save_event(fileSyncState)

                val fileInfo = FileInfo(file.path, FileSyncType.DELETE)

                scope.launch {
                    actionsQueue.send(fileInfo)
                }
            }

            var itemCount = 0;
            for (for_download in param.added_files)
            {
                val path = fileManager.joinToString(for_download.file_name)
                val fileInfo = FileInfo(path,FileSyncType.DOWNLOAD, for_download.size)
                corrent_count++
                itemCount++
                val fileSyncState = FileSyncState(path, FileSyncType.DOWNLOAD, "$corrent_count/$eventsCount")
                _fileState_eventManager.save_event(fileSyncState)

                scope.launch {
                    actionsQueue.send(fileInfo)
                }
            }

            for (for_upload in param.uploaded_files)
            {
                val path = fileManager.joinToString(for_upload.file_name)
                val fileInfo = fileManager.getFileInfoForUpload(path)

                corrent_count++
                val fileSyncState = FileSyncState(fileInfo.second, FileSyncType.UPLOAD, "$corrent_count/$eventsCount")

                val fileInfo2 = FileInfo(path, FileSyncType.UPLOAD)
                _fileState_eventManager.save_event(fileSyncState)
                scope.launch {
                    actionsQueue.send(fileInfo2)
                }
            }

            for (for_update in param.updated_files)
            {
                val path = fileManager.joinToString(for_update.file_name)
                corrent_count++
                val fileSyncState = FileSyncState(path, FileSyncType.UPDATE, "$corrent_count/$eventsCount")
                _fileState_eventManager.save_event(fileSyncState)
                val fileInfo = FileInfo(path, FileSyncType.UPDATE)

                scope.launch {
                    actionsQueue.send(fileInfo)
                }
            }

            startProcessing()
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    fun startProcessing() {
        scope.launch {

            /**ALL**/
            launch(Dispatchers.IO) {
                while (true) {
                    val item = actionsQueue.receive()

                    val job = launch {
                        if (item.type == FileSyncType.DOWNLOAD)
                        {
                            download_file(item)
                            update_process()
                        }

                        if (item.type == FileSyncType.DELETE)
                        {
                            remove_file(item)
                            update_process()
                        }

                        if (item.type == FileSyncType.UPLOAD)
                        {
                            upload_file(item)
                            update_process()
                        }

                        if (item.type == FileSyncType.UPDATE)
                        {
                            update_file(item)
                            update_process()
                        }
                    }
                    job.join()
                }
            }
        }
    }

    private suspend fun download_file(item: FileInfo)
    {
        try {
            Log.i(TAG, "Waiting download ${item.name}")

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

    private suspend fun remove_file(fileInfo: FileInfo)
    {
        try {
            val item = File(fileInfo.name)
            if (item.exists()) {
                if (item.delete())
                {
                    _fileState_eventManager.save_event(FileSyncState(item.path, FileSyncType.DELETE, String(),100))
                }
                else
                {
                    // TODO: Log it
                }
            }

        } catch (e: Throwable) {
            println(e)
            e.printStackTrace()
        }
    }

    private suspend fun update_file(baseFileInfo: FileInfo)
    {
        try {
            val file = fileManager.getFullPath(baseFileInfo.name) ?: return

            val removeFileInfo =  FileInfo(file.path, baseFileInfo.type, baseFileInfo.sizeBytes)
            val item = File(removeFileInfo.name)
            if (item.exists())
                item.delete()

            Log.i(TAG, "Waiting download ${baseFileInfo.name}")

            val response = _connectionManager.download(baseFileInfo.name)

            if (!response.isSuccessful)
                throw Exception("Attachment not found!")

            val inputStream = response.body()?.byteStream()

            inputStream?.let { stream ->
                val streamAndUri = fileManager.getFileOutputStreamAndURI(baseFileInfo.name)
                if (streamAndUri.first != null) {
                    writeResponseStreamToDisk(baseFileInfo, streamAndUri.second, streamAndUri.first!!, stream)
                }
            }
        } catch (e: Throwable) {
            println(e)
            e.printStackTrace()
        }
    }

    private suspend fun upload_file(fileInfo: FileInfo)
    {
        try {
            val item = fileManager.getFileInfoForUpload(fileInfo.name)

            val mediaType = MediaType.parse("application/octet-stream")

            val inputStream = File(item.first.name).inputStream()

            Log.i(TAG, "Waiting upload ${fileInfo.name}")

            val requestBody = inputStream.asRequestBodyWithProgress(
                contentType = mediaType,
                contentLength = item.first.sizeBytes,
                progressCallback = { progress ->
                    _fileState_eventManager.save_event(FileSyncState(item.second, FileSyncType.UPLOAD, String(), progress))
                    Log.i(TAG, "Process ${progress} upload ${fileInfo.name}")
                },
                errorCallback = { e ->
                    println(e)
                    coroutineContext.cancelChildren()
                }
            )

            requestBody.let { body ->
                val response = _connectionManager.upload(item.second, body)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // TODO: Log it
                    } else {
                        // TODO: Log it
                    }
                }
            }
        } catch (e: Throwable) {
            println(e)
            e.printStackTrace()
        }
    }

    fun InputStream.asRequestBodyWithProgress(
        contentType: MediaType? = null,
        contentLength: Long,
        progressCallback: ((progress: Int) -> Unit)?,
        errorCallback: ((e: Throwable) -> Unit)?
    ): RequestBody {
        return object : RequestBody() {
            override fun contentType() = contentType

            override fun contentLength(): Long = try {
                contentLength
            } catch (e: IOException) {
                e.printStackTrace()
                0
            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
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

                        if (lastProgress != progress) {
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

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun writeResponseStreamToDisk(fileInfo: FileInfo, uri: Uri, outputStream: OutputStream, inputStream: InputStream) {
        var fileSizeDownloaded = 0L

        try {
            val fileReader = ByteArray(10240)
            var lastProgress = -1

            Log.i(TAG, "Starting download ${fileInfo.name}")
            while (true) {
                val read = inputStream.read(fileReader)
                if (read == -1) {
                    break
                }
                outputStream.write(fileReader, 0, read)
                fileSizeDownloaded += read.toLong()

                val progress = (fileSizeDownloaded.toFloat() / fileInfo.sizeBytes * 100).toInt()

                if (lastProgress != progress) {
                    lastProgress = progress
                    val fileSyncState = FileSyncState(fileInfo.name, FileSyncType.DOWNLOAD, String(), progress)
                    _fileState_eventManager.save_event(fileSyncState)
                    Log.i(TAG, "Process ${progress} download ${fileInfo.name}")
                }
            }
            outputStream.flush()
            Log.i(TAG, "Ending download ${fileInfo.name}")

        } catch (ex: Exception) {
            println(ex)
        } finally {
            inputStream.close()
            outputStream.close()
        }
    }

    private fun update_process() {
        finishedCount++
        _syncStateEventManager.save_event("$finishedCount/$eventsCount")
    }
}