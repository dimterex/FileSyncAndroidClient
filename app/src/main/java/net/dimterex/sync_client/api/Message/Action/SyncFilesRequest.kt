package net.dimterex.sync_client.api.Message.Action

import net.dimterex.sync_client.api.Message.Common.BaseFileInfo
import net.dimterex.sync_client.api.MessageAttr
import com.google.gson.annotations.SerializedName
import net.dimterex.sync_client.api.interfaces.IMessage

@MessageAttr(name = "SyncFilesRequest")
class SyncFilesRequest() : IMessage {

    @SerializedName("files")
    var files : List<BaseFileInfo> = ArrayList<BaseFileInfo>()
}