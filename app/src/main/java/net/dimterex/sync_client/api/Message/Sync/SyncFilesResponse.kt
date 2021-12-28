package net.dimterex.sync_client.api.Message.Sync

import com.google.gson.annotations.SerializedName
import net.dimterex.sync_client.api.MessageAttr
import net.dimterex.sync_client.api.interfaces.IMessage

@MessageAttr(name = "SyncFilesResponse")
class SyncFilesResponse : IMessage {
    @SerializedName("added_files")
    var added_files : List<AddFileResponse> = ArrayList()

    @SerializedName("removed_files")
    var removed_files : List<RemoveFileResponse> = ArrayList()

    @SerializedName("uploaded_files")
    var uploaded_files : List<FileUploadRequest> = ArrayList()
}