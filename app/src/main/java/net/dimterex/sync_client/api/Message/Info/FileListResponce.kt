package net.dimterex.sync_client.api.Message.Info

import net.dimterex.sync_client.api.Message.Common.BaseFileInfo
import net.dimterex.sync_client.api.interfaces.IMessage
import net.dimterex.sync_client.api.MessageAttr
import com.google.gson.annotations.SerializedName

@MessageAttr(name = "FileListResponce")
class FileListResponce() : IMessage {

    @SerializedName("files")
    val files : List<BaseFileInfo> = ArrayList<BaseFileInfo>()
}