package net.dimterex.sync_client.ui.formatter

import android.content.res.Resources
import android.graphics.drawable.Drawable
import net.dimterex.sync_client.R

class ConnectionIconFormatted(private val _resources: Resources) {

    fun format (isConnected: Boolean) : String {
        if (isConnected)
            return _resources.getString(R.string.connected)
        return _resources.getString(R.string.disconnected)
    }
}