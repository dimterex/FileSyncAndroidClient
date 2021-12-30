package net.dimterex.sync_client.modules

import android.net.Uri
import net.dimterex.sync_client.api.Message.Sync.FileInfoItem
import net.dimterex.sync_client.data.FileInfo
import net.dimterex.sync_client.data.entries.FolderMappingLocalModel
import net.dimterex.sync_client.entity.FileSyncType
import java.io.*

interface FileManager {

    fun joinToString(file_path: List<String>) : String
    fun getFullPath(file_path: String) : File?

    fun getFileList(): List<FileInfoItem>

    fun getFileOutputStreamAndURI(filename: String): Pair<OutputStream?, Uri>
    fun getFileInfoForUpload(fileName: String): Pair<FileInfo, String>

    class Impl(private val _settingsManager: SettingsManager): FileManager{

        private val _folders: HashMap<FolderMappingLocalModel, File> = HashMap()

        init {
            _settingsManager.add_listener(this::onSettingsRead)
        }

        override fun joinToString(file_path: List<String>): String {
            return file_path.joinToString(File.separator)
        }

        override fun getFullPath(file_path: String) : File? {
            _settingsManager.get_folder_mapping().forEach{ x ->
                if (file_path.startsWith(x.outside_folder)) {
                    val new_file_path = getAdaptFileNameForUpload(file_path, x.inside_folder, x.outside_folder)
                    return File(new_file_path)
                }
            }

            return null
        }

        override fun getFileList(): List<FileInfoItem> {
            val fileList = ArrayList<FileInfoItem>()
            _folders.forEach { x ->
                x.value.walk().forEach { file ->
                    if (file.isFile) {
                        val file_name = getAdaptFileNameOutgoing(file.path, x.key.inside_folder, x.key.outside_folder)

                        val path = file_name.split(File.separator)
                        val fileInfoItem = FileInfoItem()
                        fileInfoItem.path = path
                        fileInfoItem.size = file.length()

                        fileList.add(fileInfoItem)
                    }
                }
            }
            return fileList
        }


        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        override fun getFileOutputStreamAndURI(
            filename: String
        ): Pair<OutputStream?, Uri> {
            _settingsManager.get_folder_mapping().forEach{ x ->

                if (filename.startsWith(x.outside_folder)) {
                    val file = File(getAdaptFileNameForUpload(filename, x.inside_folder, x.outside_folder))
                    file.parentFile.mkdirs()
                    file.createNewFile()

                    return FileOutputStream(file) to Uri.fromFile(file)
                }
            }

            return Pair<OutputStream?, Uri>(null, Uri.EMPTY)
        }

        override fun getFileInfoForUpload(fileName: String): Pair<FileInfo, String> {
            _settingsManager.get_folder_mapping().forEach{ x ->

                if (fileName.startsWith(x.outside_folder)) {
                    val new_file_path = getAdaptFileNameForUpload(fileName, x.inside_folder, x.outside_folder)
                    val file = File(new_file_path)
                    return FileInfo(new_file_path, FileSyncType.UPLOAD, file.length()) to fileName
                }
            }

            return FileInfo(fileName, FileSyncType.UPLOAD) to fileName

        }

        private fun getAdaptFileNameOutgoing(full_path: String, inside_path: String, outside: String ) : String {
            return full_path.replace(inside_path, outside)
        }

        private fun getAdaptFileNameForUpload(full_path: String, inside_path: String, outside: String ) : String {
            return full_path.replace(outside, inside_path)
        }

        private fun onSettingsRead() {
            _settingsManager.get_folder_mapping().forEach { folder ->
                _folders[folder] = File(folder.inside_folder)
            }
        }
    }

}