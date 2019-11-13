package net.dimterex.sync_client.api.Message.Action

import net.dimterex.sync_client.api.MessageAttr
import com.google.gson.annotations.SerializedName
import net.dimterex.sync_client.api.Message.Common.BaseFileInfo

@MessageAttr(name = "FileAddResponce")
open class AddFileResponce() : BaseFileInfo() {

    @SerializedName("stream")
    val stream : String = String()

    @SerializedName("count")
    val count : Long = 0

    @SerializedName("current")
    val current : Long = 0
}