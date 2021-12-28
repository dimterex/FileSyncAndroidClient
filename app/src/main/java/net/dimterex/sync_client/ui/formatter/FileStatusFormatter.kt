package net.dimterex.sync_client.ui.formatter

import android.content.res.Resources
import net.dimterex.sync_client.R

class FileStatusFormatter(private val _resources: Resources) {
    fun format(process: Int): String {

        if (process == 0)
            return _resources.getString(R.string.waiting)

        if (process == 100)
            return _resources.getString(R.string.done)


        return "$process %"
    }
}