package net.dimterex.sync_client.api.Message.Common

import net.dimterex.sync_client.api.interfaces.IMessage
import com.google.gson.annotations.SerializedName

open class BaseFileInfo : IMessage {

    @SerializedName("file_name")
    var file_name : String = ""

    @SerializedName("file_path")
    var file_path : List<String> = ArrayList<String>()
}