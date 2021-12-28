package net.dimterex.sync_client.api.Message.Sync

import net.dimterex.sync_client.api.MessageAttr
import com.google.gson.annotations.SerializedName
import net.dimterex.sync_client.api.interfaces.IMessage

@MessageAttr(name = "SyncFilesRequest")
class SyncFilesRequest : IMessage {

    @SerializedName("files")
    var files : List<String> = ArrayList()
}