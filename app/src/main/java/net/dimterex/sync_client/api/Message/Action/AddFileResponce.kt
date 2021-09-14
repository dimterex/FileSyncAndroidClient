package net.dimterex.sync_client.api.Message.Action

import net.dimterex.sync_client.api.MessageAttr
import com.google.gson.annotations.SerializedName
import net.dimterex.sync_client.api.Message.Common.BaseFileInfo

@MessageAttr(name = "FileAddResponce")
open class AddFileResponce() : BaseFileInfo() {

    @SerializedName("size")
    val size : Long = 0
}