package net.dimterex.sync_client.api.Message.Sync

import FileDataBaseAddResponse
import com.google.gson.annotations.SerializedName
import net.dimterex.sync_client.api.MessageAttr
import net.dimterex.sync_client.api.interfaces.IMessage

@MessageAttr(name = "sync_state_files_response")
class SyncStateFilesResponse : IMessage {
    @SerializedName("added_files")
    var added_files : List<AddFileResponse> = ArrayList()

    @SerializedName("removed_files")
    var removed_files : List<RemoveFileResponse> = ArrayList()

    @SerializedName("uploaded_files")
    var uploaded_files : List<FileUploadResponse> = ArrayList()

    @SerializedName("updated_files")
    var updated_files : List<FileUpdatedResponse> = ArrayList()

    @SerializedName("server_removed_files")
    var server_removed_files : List<FileServerRemovedResponse> = ArrayList()

    @SerializedName("database_added_files")
    var database_added_files : List<FileDataBaseAddResponse> = ArrayList()
}