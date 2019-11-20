package net.dimterex.sync_client.api.Message

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

class MessageContainer {

    @SerializedName("Type")
    var identifier: String = ""

    @SerializedName("Value")
    var content: JsonObject? = null
}
