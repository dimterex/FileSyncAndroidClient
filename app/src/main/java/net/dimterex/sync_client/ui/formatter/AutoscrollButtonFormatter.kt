package net.dimterex.sync_client.ui.formatter

class AutoscrollButtonFormatter() {

    fun format (isAutoscrollToPositionEnabled: Boolean) : Int {
        if (isAutoscrollToPositionEnabled)
            return android.R.drawable.checkbox_on_background
        return android.R.drawable.checkbox_off_background
    }
}