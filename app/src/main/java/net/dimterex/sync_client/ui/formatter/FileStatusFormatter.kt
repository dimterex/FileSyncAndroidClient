package net.dimterex.sync_client.ui.formatter

import android.content.res.Resources
import android.graphics.Color

class FileStatusFormatter(private val _resources: Resources) {
    fun format(process: Int): Int {
        if (process > 100) {
            return Color.parseColor("#de8068") // Color.RED
        }

        if (process == 100) {
            return Color.parseColor("#60e087")  // Color.GREEN
        }

        if (process == 0) {
            return Color.GRAY
        }

        return Color.parseColor("#59bfde") // Blue
    }
}