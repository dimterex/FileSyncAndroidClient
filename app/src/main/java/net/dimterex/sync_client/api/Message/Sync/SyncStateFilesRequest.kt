package net.dimterex.sync_client.api.Message.Sync

import net.dimterex.sync_client.api.MessageAttr
import com.google.gson.annotations.SerializedName
import net.dimterex.sync_client.api.interfaces.IMessage

@MessageAttr(name = "sync_state_files_request")
class SyncStateFilesRequest : IMessage {

    @SerializedName("folders")
    var folders : List<FolderItemInfo> = ArrayList()
}