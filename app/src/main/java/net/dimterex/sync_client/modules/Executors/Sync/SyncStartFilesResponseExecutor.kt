package net.dimterex.sync_client.modules.Executors.Sync

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import net.dimterex.sync_client.api.Message.Sync.SyncStartFilesResponse
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.data.FileInfo
import net.dimterex.sync_client.data.ScopeFactory
import net.dimterex.sync_client.entity.FileSyncState
import net.dimterex.sync_client.entity.FileSyncType
import net.dimterex.sync_client.modules.*
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class SyncStartFilesResponseExecutor(private val _fileManager: FileManager,
                                     private val _fileState_eventManager: FileStateEventManager,
                                     private val _syncStateEventManager: SyncStateEventManager,
                                     private val _connectionManager: ConnectionManager,
                                     private val _syncStateManager: SyncStateManager,
                                     private val _scopeFactory: ScopeFactory
) : IExecute<SyncStartFilesResponse> {

    private val TAG = this::class.java.name

    private val _actionsQueue = Channel<FileInfo>()
    private val _scope: CoroutineScope = _scopeFactory.getScope()
    private var _eventsCount = 0
    private var _finishedCount = 0

    private var _isSyncEnabled: Boolean = false

    override fun Execute(param: SyncStartFilesResponse) {
        val syncStateFilesResponse = _syncStateManager.syncStateFilesResponse ?: return

        _syncStateManager.subscribeStateChange(this::onStateChange)

        _eventsCount = syncStateFilesResponse.added_files.count() +
                syncStateFilesResponse.removed_files.count() +
                syncStateFilesResponse.uploaded_files.count() +
                syncStateFilesResponse.updated_files.count()

        _finishedCount = 0

        if (_eventsCount == 0) {
            _syncStateEventManager.save_event("Done")
            return
        }

        var corrent_count = 0

        for (for_remove in syncStateFilesResponse.removed_files)
        {
            val path = _fileManager.joinToString(for_remove.file_name)
            val insideFilePath = _fileManager.getInsideFilePath(path) ?: continue

            corrent_count++
            val fileSyncState = FileSyncState(insideFilePath.path, path, FileSyncType.DELETE, "$corrent_count/$_eventsCount", 0)
            _fileState_eventManager.save_event(fileSyncState)

            val fileInfo = FileInfo(insideFilePath.path, FileSyncType.DELETE)

            _scope.launch {
                _actionsQueue.send(fileInfo)
            }
        }

        for (for_download in syncStateFilesResponse.added_files)
        {
            val path = _fileManager.joinToString(for_download.file_name)
            val insideFilePath = _fileManager.getInsideFilePath(path) ?: continue

            val fileInfo = FileInfo(path, FileSyncType.DOWNLOAD, for_download.size)
            corrent_count++
            val fileSyncState = FileSyncState(insideFilePath.path, path, FileSyncType.DOWNLOAD, "$corrent_count/$_eventsCount", 0)
            _fileState_eventManager.save_event(fileSyncState)

            _scope.launch {
                _actionsQueue.send(fileInfo)
            }
        }

        for (for_upload in syncStateFilesResponse.uploaded_files)
        {
            val path = _fileManager.joinToString(for_upload.file_name)
            val fileInfo = _fileManager.getFileInfoForUpload(path)
            val insideFilePath = _fileManager.getInsideFilePath(path) ?: continue

            corrent_count++
            val fileSyncState = FileSyncState(insideFilePath.path, fileInfo.second, FileSyncType.UPLOAD, "$corrent_count/$_eventsCount", 0)

            val fileInfo2 = FileInfo(path, FileSyncType.UPLOAD)
            _fileState_eventManager.save_event(fileSyncState)
            _scope.launch {
                _actionsQueue.send(fileInfo2)
            }
        }

        for (for_update in syncStateFilesResponse.updated_files)
        {
            val path = _fileManager.joinToString(for_update.file_name)
            val insideFilePath = _fileManager.getInsideFilePath(path) ?: continue

            corrent_count++
            val fileSyncState = FileSyncState(insideFilePath.path, path, FileSyncType.UPDATE, "$corrent_count/$_eventsCount", 0)
            _fileState_eventManager.save_event(fileSyncState)
            val fileInfo = FileInfo(path, FileSyncType.UPDATE, for_update.size)

            _scope.launch {
                _actionsQueue.send(fileInfo)
            }
        }

        onStateChange(true);
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    fun startProcessing() {
        _scope.launch {

            /**ALL**/
            launch(Dispatchers.IO) {
                while (true) {
                    if (!_isSyncEnabled)
                        break

                    val item = _actionsQueue.receive()

                    val job = launch {
                        when(item.type) {
                            FileSyncType.DOWNLOAD -> download_file(item)
                            FileSyncType.UPLOAD -> upload_file(item)
                            FileSyncType.DELETE -> remove_file(item)
                            FileSyncType.UPDATE -> update_file(item)
                        }
                        update_process()

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

            val response = _connectionManager.download(_fileManager.toPathArray(item.name))

            if (!response.isSuccessful)
                throw Exception("Attachment not found!")

            val file = _fileManager.getInsideFilePath(item.name) ?: return
            val inputStream = response.body()?.byteStream()

            inputStream?.let { stream ->
                val streamAndUri = _fileManager.getFileOutputStreamAndURI(item.name)
                if (streamAndUri.first != null) {
                    writeResponseStreamToDisk(item, file.path, streamAndUri.first!!, stream)
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
                    _fileState_eventManager.save_event(FileSyncState(item.path, item.path, FileSyncType.DELETE, String(),100))
                }
                else
                {
                    Log.w(TAG, "Cant delete ${item}")
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
            val file = _fileManager.getInsideFilePath(baseFileInfo.name) ?: return

            Log.i(TAG, "Waiting update ${baseFileInfo.name}")

            val fileSyncState = FileSyncState(file.path, baseFileInfo.name, FileSyncType.UPDATE, String(), 0)
            _fileState_eventManager.save_event(fileSyncState)

            val removeFileInfo = FileInfo(file.path, baseFileInfo.type, baseFileInfo.sizeBytes)
            val item = File(removeFileInfo.name)
            if (item.exists())
                item.delete()

            val response = _connectionManager.download(_fileManager.toPathArray(baseFileInfo.name))

            if (!response.isSuccessful)
                throw Exception("Attachment not found!")

            val inputStream = response.body()?.byteStream()

            inputStream?.let { stream ->
                val streamAndUri = _fileManager.getFileOutputStreamAndURI(baseFileInfo.name)
                if (streamAndUri.first != null) {
                    writeResponseStreamToDisk(baseFileInfo, file.path, streamAndUri.first!!, stream)
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
            val item = _fileManager.getFileInfoForUpload(fileInfo.name)

            val mediaType = MediaType.parse("application/octet-stream")

            val inputStream = File(item.first.name).inputStream()

            Log.i(TAG, "Waiting upload ${fileInfo.name}")

            val requestBody = inputStream.asRequestBodyWithProgress(
                contentType = mediaType,
                contentLength = item.first.sizeBytes,
                progressCallback = { progress ->
                    _fileState_eventManager.save_event(FileSyncState(item.first.name, item.second, FileSyncType.UPLOAD, String(), progress))
                    Log.i(TAG, "Process ${progress} upload ${fileInfo.name}")
                },
                errorCallback = { e ->
                    println(e)
                    GlobalScope.coroutineContext.cancelChildren()
                }
            )

            requestBody.let { body ->

                val response = _connectionManager.upload(_fileManager.toPathArray(item.second), body)

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
    private suspend fun writeResponseStreamToDisk(fileInfo: FileInfo, insideFilePath: String, outputStream: OutputStream, inputStream: InputStream) {
        var fileSizeDownloaded = 0L
        var PART_SIZE = 10240

        try {
            val fileReader = ByteArray(PART_SIZE)
            var lastProgress = -1

            Log.i(TAG, "Starting download ${fileInfo.name}")
            while (true) {
                val read = inputStream.read(fileReader)
                if (read == -1) {
                    break
                }
                outputStream.write(fileReader, 0, read)
                fileSizeDownloaded += read.toLong()

                var progress = (fileSizeDownloaded.toFloat() / fileInfo.sizeBytes * 100).toInt()

                if (fileInfo.sizeBytes < PART_SIZE) {
                    progress = 100;
                }

                if (lastProgress != progress) {
                    lastProgress = progress
                    val fileSyncState = FileSyncState(insideFilePath, fileInfo.name, FileSyncType.DOWNLOAD, String(), progress)
                    _fileState_eventManager.save_event(fileSyncState)
                    Log.d(TAG, "Process ${progress} download ${fileInfo.name}")
                }
            }
            outputStream.flush()
            Log.i(TAG, "Ending download ${fileInfo.name}")

        } catch (ex: Exception) {
            Log.e(TAG, ex.toString())
        } finally {
            inputStream.close()
            outputStream.close()
        }
    }

    private fun update_process() {
        _finishedCount++
        _syncStateEventManager.save_event("$_finishedCount/$_eventsCount")
    }

    private fun onStateChange(isSyncEnabled: Boolean) {
        _isSyncEnabled = isSyncEnabled

        if (_isSyncEnabled)
            startProcessing()
    }
}