package net.dimterex.sync_client.api.Message.Temp

import net.dimterex.sync_client.api.interfaces.IMessage
import net.dimterex.sync_client.api.MessageAttr
import com.google.gson.annotations.SerializedName

@MessageAttr(name = "SaveFileResponce")
class SaveFileResponce() : IMessage {

    @SerializedName("file_name")
    val file_name : String = String()

    @SerializedName("message")
    val message : String = String()

}