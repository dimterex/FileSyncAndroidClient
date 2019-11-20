package net.dimterex.sync_client.modules

import net.dimterex.sync_client.api.Message.Common.BaseFileInfo
import java.io.File

interface FileManager {
    fun setDefaultDirectory(pathFile: File)

    fun getFullPath(file_path: List<String>, use_mkdir: Boolean) : File?

    fun getFileList(): List<BaseFileInfo>

    fun set_sync_folder(newSyncFolder: File)

    class Impl(private val _settingsManager: SettingsManager): FileManager{

        var defaultPath: File = File(_settingsManager.get_settings().defaultFolder)

        override fun setDefaultDirectory(pathFile: File) {
            defaultPath = pathFile
        }

        override fun getFullPath(file_path: List<String> ,use_mkdir: Boolean) : File? {
            var filePath = defaultPath

            for (path in file_path) {
                filePath = File(filePath, path)

                if (use_mkdir && !filePath.exists())
                    filePath.mkdir()
            }

            return filePath
        }

        override fun getFileList(): List<BaseFileInfo> {
            var fileList = ArrayList<BaseFileInfo>()

            defaultPath.walk().forEach {
                if (it.isFile)
                {
                    var baseFileInfo = BaseFileInfo()
                    baseFileInfo.file_name = it.name
                    var folders = it.parent.replace(defaultPath!!.absolutePath, String())
                    baseFileInfo.file_path = folders.split(File.separatorChar)
                    fileList.add(baseFileInfo)
                }
            }
            return fileList
        }

        override fun set_sync_folder(newSyncFolder: File) {
            defaultPath = newSyncFolder
        }
    }
}