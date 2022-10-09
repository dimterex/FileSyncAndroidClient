package net.dimterex.sync_client.api.Message.Connection

import com.google.gson.annotations.SerializedName
import net.dimterex.sync_client.api.MessageAttr
import net.dimterex.sync_client.api.interfaces.IMessage

@MessageAttr(name = "connection_response")
class ConnectionResponse : IMessage {

    @SerializedName("token")
    var token: String = String()

    @SerializedName("shared_folders")
    var shared_folders: ArrayList<SharedFolder> = ArrayList()
}