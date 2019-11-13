package net.dimterex.sync_client.modules.Executors.Action

import net.dimterex.sync_client.api.Message.Action.AddFileResponce
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.modules.EventLoggerManager
import net.dimterex.sync_client.modules.FileManager
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

class AddFileResponceExecutor(val fileManager: FileManager, private val _eventLoggerManager: EventLoggerManager) : IExecute<AddFileResponce> {

    var _savedStreams : Hashtable<String, OutputStream> = Hashtable<String, OutputStream>()

    override fun Execute(param: AddFileResponce) {
        var outputStream: OutputStream? = getOutputStream(param)
        var byteArray = android.util.Base64.decode(param.stream, android.util.Base64.DEFAULT)
        outputStream?.write(byteArray)
        outputStream?.flush()

        if (param.current == param.count) {
            outputStream?.close()
            _savedStreams.remove(param.file_name)
        }

        _eventLoggerManager.save_event(param.file_name + " was saved "+ param.current + "/" + param.count)
    }

    private fun getOutputStream(param: AddFileResponce): OutputStream? {
        var outputStream = _savedStreams[param.file_name]
        if (outputStream != null)
            return outputStream

        var filePath = fileManager.getFullPath(param.file_path, true)

        var full_path = File(filePath, param.file_name)
        if (full_path.exists())
            full_path.delete()

        full_path.createNewFile()
        outputStream = FileOutputStream(full_path)
        _savedStreams[param.file_name] = outputStream
        return outputStream
    }
}