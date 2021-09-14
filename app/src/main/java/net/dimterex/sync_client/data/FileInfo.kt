package net.dimterex.sync_client.data

import android.net.Uri
import kotlinx.coroutines.Job

data class FileInfo(
    val name: String,
    val uri: Uri? = null,
    val sizeBytes: Long = 0,
    val job: Job? = null,
    val state: FileState = WAITING()
)

sealed class FileState()
data class READY_DOWNLOAD(val any: Any? = null) : FileState()
data class READY_UPLOAD(val any: Any? = null) : FileState()
data class PROGRESS(val progress: Int = 0) : FileState() //заявка обрабатывается в данный момент
data class WAITING(val value: Int = 3) : FileState() //ожидание обработки
data class CANCELLED(val value: Int = 4) : FileState() //обработка прекращена пользователем, но очередь еще не дошла
data class FAIL(val error: Throwable? = null) : FileState() // файла нет в очереди и у него осталось расширение tmp или была ошибка при upload