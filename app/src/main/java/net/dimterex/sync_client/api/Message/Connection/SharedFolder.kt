package net.dimterex.sync_client.api.Message.Connection

import com.google.gson.annotations.SerializedName

class SharedFolder {

    @SerializedName("path")
    var path : List<String> = ArrayList()

}