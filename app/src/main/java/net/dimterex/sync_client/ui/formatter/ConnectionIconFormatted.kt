package net.dimterex.sync_client.ui.formatter

import android.content.res.Resources
import android.graphics.drawable.Drawable
import net.dimterex.sync_client.R

class ConnectionIconFormatted(private val _resources: Resources) {

    fun format (isConnected: Boolean) : Int {
        if (isConnected)
            return R.drawable.ic_connected
//        return _resources.getDrawable(R.drawable.ic_not_connected)
        return R.drawable.ic_not_connected
    }

}