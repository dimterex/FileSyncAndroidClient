package net.dimterex.sync_client.api.Message.Sync

import com.google.gson.annotations.SerializedName

class FileInfoItem {

    @SerializedName("path")
    var path : List<String> = ArrayList()

    @SerializedName("size")
    var size : Long = 0

    @SerializedName("timestamp")
    var timestamp: Long = 0
}