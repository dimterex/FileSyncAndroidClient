package net.dimterex.sync_client.api.Message.Sync

import com.google.gson.annotations.SerializedName

class FolderItemInfo {
    @SerializedName("files")
    var files : ArrayList<FileInfoItem> = ArrayList()

    @SerializedName("path")
    var path: List<String> = ArrayList()
}