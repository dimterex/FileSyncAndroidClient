package net.dimterex.sync_client.api.Message.Connection

import com.google.gson.annotations.SerializedName
import net.dimterex.sync_client.api.MessageAttr
import net.dimterex.sync_client.api.interfaces.IMessage

@MessageAttr(name = "connection_request")
class ConnectionRequest : IMessage {

    @SerializedName("login")
    var login: String = String()

    @SerializedName("password")
    var password: String = String()
}