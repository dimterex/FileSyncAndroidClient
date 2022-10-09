package net.dimterex.sync_client.api.Message.Sync

import net.dimterex.sync_client.api.MessageAttr
import com.google.gson.annotations.SerializedName
import net.dimterex.sync_client.api.interfaces.IMessage

@MessageAttr(name = "file_add_response")
class AddFileResponse : IMessage {

    @SerializedName("size")
    val size : Long = 0

    @SerializedName("file_name")
    var file_name : List<String> = ArrayList()
}