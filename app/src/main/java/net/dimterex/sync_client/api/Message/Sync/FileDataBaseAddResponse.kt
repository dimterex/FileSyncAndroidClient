import com.google.gson.annotations.SerializedName
import net.dimterex.sync_client.api.MessageAttr
import net.dimterex.sync_client.api.interfaces.IMessage

@MessageAttr(name = "file_database_add_response")
class FileDataBaseAddResponse : IMessage {

    @SerializedName("file_name")
    var file_name : List<String> = ArrayList()
}