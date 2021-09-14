package net.dimterex.sync_client.modules

import android.net.Uri
import net.dimterex.sync_client.api.Message.Common.BaseFileInfo
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

interface FileManager {
    fun setDefaultDirectory(pathFile: File)

    fun getFullPath(file_path: String, use_mkdir: Boolean) : File?

    fun getFileList(): List<BaseFileInfo>

    fun set_sync_folder(newSyncFolder: File)

    fun getFileOutputStreamAndURI(filename: String): Pair<OutputStream?, Uri>

    class Impl(private val _settingsManager: SettingsManager): FileManager{

        private val _folders: Map<String, File> = HashMap()

        init {
            _settingsManager.add_listener(this::onSettingsReaded)
        }

        override fun setDefaultDirectory(pathFile: File) {
//            defaultPath = pathFile
        }

        override fun getFullPath(file_path: String ,use_mkdir: Boolean) : File? {

            var filePath: File?

            _settingsManager.get_folder_mapping().forEach{ x ->

                if (file_path.startsWith(x.outside_folder)) {

                    val new_file_path = getAdaptFileName(file_path, x.inside_folder, x.outside_folder)

                    filePath = File(new_file_path)

                    if (use_mkdir && !filePath!!.exists())
                        filePath!!.mkdir()

                    return filePath

                }
            }

            return null;
        }

        override fun getFileList(): List<BaseFileInfo> {
            var fileList = ArrayList<BaseFileInfo>()
            _folders.forEach { x ->
                x.value.walk().forEach {
                    if (it.isFile) {
                        var baseFileInfo = BaseFileInfo()
                        baseFileInfo.file_name = it.name

                        var folders = it.parent.replace(x.key, String())
//                        baseFileInfo.file_path = folders.split(File.separatorChar)

                        fileList.add(baseFileInfo)
                    }
                }
            }
            return fileList
        }

        override fun set_sync_folder(newSyncFolder: File) {
//            defaultPath = newSyncFolder
        }

        override fun getFileOutputStreamAndURI(
            filename: String
        ): Pair<OutputStream?, Uri> {
            _settingsManager.get_folder_mapping().forEach{ x ->

                if (filename.startsWith(x.outside_folder)) {
                    val file = File(getAdaptFileName(filename, x.inside_folder, x.outside_folder))
                    return FileOutputStream(file) to Uri.fromFile(file)
                }
            }

            return Pair<OutputStream?, Uri>(null, Uri.EMPTY)
        }

        private fun getAdaptFileName(full_path: String, inside_path: String, outside: String ) : String {

            return full_path.replace(outside, inside_path).replace("\\", "/");
        }

        private fun onSettingsReaded() {
            _settingsManager.get_folder_mapping().forEach { folder ->
                _folders.plus(Pair(folder.inside_folder, File(folder.inside_folder)))
            }
        }
    }

}