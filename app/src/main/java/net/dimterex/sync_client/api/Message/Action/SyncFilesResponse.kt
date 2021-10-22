package net.dimterex.sync_client.api.Message.Action

import com.google.gson.annotations.SerializedName
import net.dimterex.sync_client.api.interfaces.IMessage

class SyncFilesResponse : IMessage {
    @SerializedName("added_files")
    var added_files : List<AddFileResponce> = ArrayList()

    @SerializedName("removed_files")
    var removed_files : List<RemoveFileResponce> = ArrayList()

    @SerializedName("uploaded_files")
    var uploaded_files : List<FileUploadRequest> = ArrayList()
}