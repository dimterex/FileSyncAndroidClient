package net.dimterex.sync_client.api.Message.Sync

import com.google.gson.annotations.SerializedName
import net.dimterex.sync_client.api.MessageAttr
import net.dimterex.sync_client.api.interfaces.IMessage

@MessageAttr(name = "file_upload_response")
class FileUploadResponse : IMessage {

    @SerializedName("file_name")
    var file_name : List<String> = ArrayList()
}