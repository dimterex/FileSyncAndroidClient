package net.dimterex.sync_client.api.Message.Common

import com.google.gson.annotations.SerializedName
import net.dimterex.sync_client.api.MessageAttr

@MessageAttr(name = "save_file_info")
open class SaveFileInfo : BaseFileInfo() {

    @SerializedName("size")
    var size : Double = 0.0
}